const { GoogleGenerativeAI } = require("@google/generative-ai");
const { GoogleAICacheManager } = require("@google/generative-ai/server"); // Thêm dòng này để quản lý Cache
const fs = require("fs");
const path = require("path");
const readline = require("readline");

const API_KEY = "AIzaSyDJpDDGEtA8zjnd6IweyHzHrpL7lg6d68Y";

// Khởi tạo các instance cần thiết từ thư viện cũ của bạn
const genAI = new GoogleGenerativeAI(API_KEY);
const cacheManager = new GoogleAICacheManager(API_KEY);

function getContext(dirPath, extensions = [".kt", ".xml"]) {
    let files = "";
    if (!fs.existsSync(dirPath)) return files;
    const list = fs.readdirSync(dirPath);
    list.forEach(file => {
        const fullPath = path.join(dirPath, file);
        if (fs.statSync(fullPath).isDirectory()) {
            files += getContext(fullPath, extensions);
        } else if (extensions.some(ext => file.endsWith(ext))) {
            const relativePath = path.relative(process.cwd(), fullPath);
            files += `\n--- FILE: ${relativePath} ---\n${fs.readFileSync(fullPath, 'utf8')}\n`;
        }
    });
    return files;
}

function askQuestion(query) {
    const rl = readline.createInterface({
        input: process.stdin,
        output: process.stdout,
    });
    return new Promise(resolve => rl.question(query, ans => {
        rl.close();
        resolve(ans);
    }));
}

async function run(userRequest) {
    try {
        console.log("📦 Đọc tài liệu kiến trúc phát triển (Skills & Rules)...");
        const rulesContent = getContext("./skills", [".md", ".txt"]);

        let cacheName = null;
        if (rulesContent.trim()) {
            console.log("💾 Đang khởi tạo/Cập nhật dữ liệu quy tắc vào Context Cache...");
            const cache = await cacheManager.create({
                model: "models/gemini-2.5-flash",
                displayName: "daily_english_rules_cache",
                ttlSeconds: 10800,
                contents: [{ role: "user", parts: [{ text: rulesContent }] }]
            });
            cacheName = cache.name;
        }

        // ====================================================================
        // 🛠️ ĐIỂM CẢI TIẾN: QUÉT ĐỒNG THỜI CẢ DATA, DOMAIN, PRESENTATION CỦA TỪ ĐIỂN
        // ====================================================================
        console.log("🔍 Đang quét toàn bộ các layer liên quan đến Dictionary...");

        let projectCode = "";
        projectCode += "\n--- LAYER: DATA ---\n" + getContext("./app/src/main/java/com/codework/daily_english/data");
        projectCode += "\n--- LAYER: DOMAIN ---\n" + getContext("./app/src/main/java/com/codework/daily_english/domain");
        projectCode += "\n--- LAYER: PRESENTATION ---\n" + getContext("./app/src/main/java/com/codework/daily_english/presentation");

        const finalPrompt = `Code hiện tại của dự án:\n${projectCode}\n\nYêu cầu tính năng mới: ${userRequest}`;

        console.log("🚀 Gemini 2.5 Flash đang xử lý yêu cầu cấu trúc code...");

        const modelConfig = {
            model: "gemini-2.5-flash",
            systemInstruction: `Bạn là Senior Android Dev.
            NHIỆM VỤ: Khi thiết kế trang/feature, phải trả về code bọc trong tag FILE.
            ĐỊNH DẠNG BẮT BUỘC:
            [FILE: đường/dẫn/tới/file.kt]
            nội dung code
            [END FILE]
            LƯU Ý: KHÔNG giải thích, KHÔNG chào hỏi. Chỉ trả về các khối FILE.`
        };

        if (cacheName) {
            modelConfig.version = "v1beta";
            modelConfig.cachedContent = cacheName;
        }

        const model = genAI.getGenerativeModel(modelConfig);
        const result = await model.generateContent(finalPrompt);
        const output = result.response.text();

        const fileRegex = /\[FILE: (.*?)\]([\s\S]*?)\[END FILE\]/g;
        let match;
        const tasks = [];

        while ((match = fileRegex.exec(output)) !== null) {
            tasks.push({
                relativePath: match[1].trim(),
                content: match[2].trim()
            });
        }

        // Cải tiến chốt chặn để không bị im lặng nếu rỗng
        if (tasks.length === 0) {
            console.log("\n💡 [Phản hồi từ AI - Không có cấu trúc FILE]:");
            console.log("----------------------------------------------");
            console.log(output || "AI không trả về nội dung nào.");
            console.log("----------------------------------------------");
            return;
        }

        console.log("\n==============================================");
        console.log("📋 DANH SÁCH FILE AI ĐỀ XUẤT THAY ĐỔI/TẠO MỚI:");
        console.log("==============================================");
        tasks.forEach((task, index) => {
            console.log(` [${index + 1}] 📄 ${task.relativePath}`);
        });
        console.log("==============================================\n");

        console.log("💾 Đang ghi bản xem trước vào thư mục './ai_preview'...");
        tasks.forEach(task => {
            const previewPath = path.resolve(process.cwd(), "ai_preview", task.relativePath);
            fs.mkdirSync(path.dirname(previewPath), { recursive: true });
            fs.writeFileSync(previewPath, task.content, 'utf8');
        });
        console.log("👉 Hãy vào thư mục 'ai_preview/' kiểm tra code trước.\n");

        // TÌM ĐOẠN CHECK ANSWER NÀY TRONG SCRIPT CỦA BẠN:
        const answer = await askQuestion("⚠️ Bạn có đồng ý cập nhật thẳng đống code này vào Project thật không? (Y/N): ");

        if (answer.trim().toLowerCase() === 'y') {
            console.log("\n🛠️ Đang tiến hành áp dụng code vào project...");
            tasks.forEach(task => {
                const realFullPath = path.resolve(process.cwd(), task.relativePath);
                fs.mkdirSync(path.dirname(realFullPath), { recursive: true });
                fs.writeFileSync(realFullPath, task.content, 'utf8');
                console.log(`  ✅ Đã ghi: ${task.relativePath}`);
                    });
            console.log("\n🎉 Hoàn tất! Dự án của bạn đã được cập nhật an toàn.");
            fs.rmSync(path.resolve(process.cwd(), "ai_preview"), { recursive: true, force: true });
        } else {
            // SỬA BLOCK ELSE NÀY ĐỂ TỰ ĐỘNG XÓA KHI ẤN NO:
            console.log("\n❌ Đã hủy bỏ thao tác. Thư mục gốc được giữ nguyên trạng!");
            console.log("🧹 Đang tự động dọn dẹp và xóa bỏ thư mục xem trước 'ai_preview/'...");

            // Thêm dòng này để xóa folder tạm ngay lập tức
            fs.rmSync(path.resolve(process.cwd(), "ai_preview"), { recursive: true, force: true });

            console.log("✨ Đã dọn dẹp xong! Project hoàn toàn sạch sẽ.");
        }

    } catch (error) {
        console.error("❌ Lỗi hệ thống:", error.message);
    }
}

run(process.argv.slice(2).join(" "));