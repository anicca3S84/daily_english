const { GoogleGenerativeAI } = require("@google/generative-ai");
const fs = require("fs");
const path = require("path");

// 1. Dán API Key từ Google AI Studio vào đây
const API_KEY = "AIzaSyDJpDDGEtA8zjnd6IweyHzHrpL7lg6d68Y";
const genAI = new GoogleGenerativeAI(API_KEY);

const model = genAI.getGenerativeModel({
    model: "gemini-3-pro-preview", // Bản Pro cực kỳ ổn định cho context lớn
    systemInstruction: `Bạn là Senior Android Dev.
    NHIỆM VỤ: Khi thiết kế trang/feature, phải trả về code bọc trong tag FILE.
    ĐỊNH DẠNG BẮT BUỘC:
    [FILE: đường/dẫn/tới/file.kt]
    nội dung code
    [END FILE]

    LƯU Ý: KHÔNG giải thích, KHÔNG chào hỏi. Chỉ trả về các khối FILE.`
});

function getContext(dirPath, files = "") {
    if (!fs.existsSync(dirPath)) return files;
    const list = fs.readdirSync(dirPath);
    list.forEach(file => {
        const fullPath = path.join(dirPath, file);
        if (fs.statSync(fullPath).isDirectory()) {
            files = getContext(fullPath, files);
        } else if (file.endsWith(".kt") || file.endsWith(".xml")) {
            const relativePath = path.relative(process.cwd(), fullPath);
            files += `\n--- FILE: ${relativePath} ---\n${fs.readFileSync(fullPath, 'utf8')}\n`;
        }
    });
    return files;
}

async function run(userRequest) {
    console.log("🔍 Đang đọc toàn bộ project...");
    // AI sẽ đọc thư mục presentation để hiểu logic MVI và Route của bạn
    const projectCode = getContext("./app/src/main/java/com/codework/daily_english/presentation");

    const prompt = `Code hiện tại:\n${projectCode}\n\nYêu cầu: ${userRequest}`;

    console.log("🚀 Gemini 3.1 Pro đang 'múa code' và tạo thư mục...");
    try {
        const result = await model.generateContent(prompt);
        const output = result.response.text();

        // Regex cải tiến để bắt được code ngay cả khi có văn bản thừa
        const fileRegex = /\[FILE: (.*?)\]([\s\S]*?)\[END FILE\]/g;
        let match;
        let count = 0;

        while ((match = fileRegex.exec(output)) !== null) {
            const filePath = match[1].trim();
            const content = match[2].trim();
            const fullPath = path.resolve(process.cwd(), filePath);

            // TỰ ĐỘNG TẠO THƯ MỤC (NẾU CHƯA CÓ)
            fs.mkdirSync(path.dirname(fullPath), { recursive: true });

            fs.writeFileSync(fullPath, content, 'utf8');
            console.log(`✅ Đã ghi file: ${filePath}`);
            count++;
        }

        if (count === 0) {
            console.log("❌ AI không trả về đúng định dạng FILE. Nội dung nhận được:\n", output);
        } else {
            console.log(`\n🎉 Xong! Đã tạo ${count} file vào project.`);
        }
    } catch (error) {
        console.error("❌ Lỗi API:", error.message);
    }
}

run(process.argv.slice(2).join(" "));