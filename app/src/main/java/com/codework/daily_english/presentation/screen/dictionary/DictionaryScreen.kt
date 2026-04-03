package com.codework.daily_english.presentation.screen.dictionary

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codework.daily_english.presentation.components.AppTopBar
import com.codework.daily_english.presentation.components.PhoneticGroup
import com.codework.daily_english.presentation.screen.dictionary.model.Word
import com.codework.daily_english.presentation.screen.dictionary.mvi.DictionaryEvent
import com.codework.daily_english.presentation.screen.dictionary.mvi.DictionaryState
import com.codework.daily_english.presentation.theme.AppBackground
import com.codework.daily_english.presentation.theme.BluePrimary

@Composable
fun DictionaryScreen(
    state: DictionaryState, // SỬA: Đổi DictionaryData thành DictionaryState
    onEvent: (DictionaryEvent) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        containerColor = AppBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            AppTopBar(
                onBackClick = onBackClick,
                onMenuClick = null,
                titleContent = {
                    Text(
                        text = "Dictionary",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(AppBackground)
        ) {

                Column(modifier = Modifier.fillMaxSize()) {

//                    DictionarySearchBar(
//                        query = data.query,
//                        onQueryChange = { onEvent(DictionaryEvent.OnSearchQueryChange(it)) },
//                        onClear = { onEvent(DictionaryEvent.OnClearSearch) }
//                    )

//                    LazyColumn(
//                        modifier = Modifier.fillMaxSize(),
//                        contentPadding = PaddingValues(16.dp),
//                        verticalArrangement = Arrangement.spacedBy(16.dp)
//                    ) {
//                        if (data.query.isNotEmpty()) {
//                            if (data.searchResults.isEmpty()) {
//                                item { EmptySearchResult(data.query) }
//                            } else {
//                                items(data.searchResults) { word ->
//                                    WordResultCard(
//                                        word = word,
//                                        onPlayAudioUk = { onEvent(DictionaryEvent.OnPlayAudio(word.word, "UK")) }, // SỬA: Thêm accent
//                                        onPlayAudioUs = { onEvent(DictionaryEvent.OnPlayAudio(word.word, "US")) }, // SỬA: Thêm accent
//                                        onToggleSave = { onEvent(DictionaryEvent.OnToggleSaveWord(word)) } // SỬA: Gắn Event
//                                    )
//                                }
//                            }
//                        } else {
//                            item {
//                                SectionTitle("Quick Actions")
//                                Spacer(modifier = Modifier.height(12.dp))
//                                QuickActionsGrid(onEvent = onEvent) // SỬA: Truyền onEvent
//                            }
//
//                            if (data.recentSearches.isNotEmpty()) {
//                                item {
//                                    SectionTitle("Recent Searches")
//                                    Spacer(modifier = Modifier.height(12.dp))
//                                    RecentSearchList(data.recentSearches)
//                                }
//                            }
//
//                            item {
//                                SectionTitle("Explore Topics")
//                                Spacer(modifier = Modifier.height(12.dp))
//                                TopicList(onEvent = onEvent) // SỬA: Truyền onEvent
//                            }
//
//                            item {
//                                Spacer(modifier = Modifier.height(8.dp))
//                                DailyQuoteCard()
//                            }
//                        }
//
//                        item { Spacer(modifier = Modifier.height(80.dp)) }
//                    }
//                }
//            }
                }
            }
        }

// SỬA: Cập nhật hàm này để nhận onEvent
//@Composable
//fun QuickActionsGrid(onEvent: (DictionaryEvent) -> Unit) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = Arrangement.spacedBy(12.dp)
//    ) {
//        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
//            QuickActionCard("Saved Words", Icons.Rounded.Bookmark, Color(0xFF3B82F6)) { onEvent(DictionaryEvent.OnQuickActionClick("Saved")) }
//            QuickActionCard("Phrasal Verbs", Icons.Rounded.Spellcheck, Color(0xFF10B981)) { onEvent(DictionaryEvent.OnQuickActionClick("Grammar")) }
//        }
//        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
//            QuickActionCard("Idioms", Icons.Rounded.Lightbulb, Color(0xFFF59E0B)) { onEvent(DictionaryEvent.OnQuickActionClick("Idioms")) }
//            QuickActionCard("Flashcards", Icons.Rounded.Quiz, Color(0xFF8B5CF6)) { onEvent(DictionaryEvent.OnQuickActionClick("Flashcards")) }
//        }
//    }
//}

        @Composable
        fun QuickActionCard(
            title: String,
            icon: ImageVector,
            color: Color,
            onClick: () -> Unit
        ) {
            Surface(
                onClick = onClick,
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 1.dp,
                border = BorderStroke(1.dp, Color(0xFFF1F5F9)),
                modifier = Modifier.fillMaxWidth().height(80.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(color.copy(alpha = 0.1f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = color,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = title,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color(0xFF334155)
                    )
                }
            }
        }

        @Composable
        fun TopicList(onEvent: (DictionaryEvent) -> Unit) {
            val topics = listOf(
                Pair("Business", "💼"), Pair("Travel", "✈️"), Pair("Food", "🍔"),
                Pair("Technology", "💻"), Pair("Health", "🏥"), Pair("Sport", "⚽")
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(topics) { (name, emoji) ->
                    Surface(
                        onClick = { onEvent(DictionaryEvent.OnTopicClick(name)) }, // SỬA: Gọi sự kiện
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.width(100.dp).height(80.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = emoji, fontSize = 24.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = name,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF475569)
                            )
                        }
                    }
                }
            }
        }

        @Composable
        fun DailyQuoteCard() {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier.background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF8E2DE2).copy(alpha = 0.1f),
                                Color(0xFF4FACFE).copy(alpha = 0.1f)
                            )
                        )
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "DAILY INSPIRATION",
                                style = MaterialTheme.typography.labelSmall,
                                color = BluePrimary,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "\"Learning never exhausts the mind.\"",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic,
                                color = Color(0xFF1F2937)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "- Leonardo da Vinci",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        Icon(
                            imageVector = Icons.Rounded.FormatQuote,
                            contentDescription = null,
                            tint = BluePrimary.copy(alpha = 0.3f),
                            modifier = Modifier.size(48.dp)
                        )
                    }
                }
            }
        }

// ==========================
// EXISTING COMPONENTS (Giữ nguyên)
// ==========================

        @Composable
        fun DictionarySearchBar(
            query: String,
            onQueryChange: (String) -> Unit,
            onClear: () -> Unit
        ) {
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Search, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                BasicTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    modifier = Modifier.weight(1f),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color(0xFF1F2937)),
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.CenterStart) {
                            if (query.isEmpty()) {
                                Text(
                                    "Search words, idioms...",
                                    color = Color.Gray,
                                    fontSize = 15.sp
                                )
                            }
                            innerTextField()
                        }
                    }
                )
                if (query.isNotEmpty()) {
                    IconButton(onClick = onClear, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, null, tint = Color.Gray)
                    }
                }
            }
        }

        @Composable
        fun WordResultCard(
            word: Word,
            onPlayAudioUk: () -> Unit,
            onPlayAudioUs: () -> Unit,
            onToggleSave: () -> Unit
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    // --- 1. HEADER (Giữ nguyên) ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = word.word,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Surface(
                            color = BluePrimary,
                            shape = RoundedCornerShape(6.dp),
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = word.type,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // --- 2. AUDIO SECTION (Giữ nguyên) ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PhoneticGroup(
                            label = "UK",
                            phonetic = word.phoneticUk,
                            mainColor = Color(0xFFE91E63),
                            onClick = onPlayAudioUk
                        )
                        Spacer(modifier = Modifier.width(32.dp))
                        PhoneticGroup(
                            label = "US",
                            phonetic = word.phoneticUs,
                            mainColor = Color(0xFF3B82F6),
                            onClick = onPlayAudioUs
                        )
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        thickness = 1.dp,
                        color = Color(0xFFE2E8F0)
                    )

                    // --- 3. DEFINITION ---
                    Text(
                        text = word.definition,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF334155),
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // --- 4. FOOTER: EXAMPLE & BOOKMARK ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom // Căn đáy để Bookmark nằm dưới
                    ) {
                        // SỬA: Dùng IntrinsicSize.Min để chiều cao Row phụ thuộc vào con cao nhất (Text)
                        Row(
                            modifier = Modifier
                                .weight(1f)
                                .height(IntrinsicSize.Min), // QUAN TRỌNG: Gạch sẽ co giãn theo text
                            verticalAlignment = Alignment.Top
                        ) {
                            // Thanh gạch dọc
                            Box(
                                modifier = Modifier
                                    .width(3.dp)
                                    .fillMaxHeight() // Chiếm hết chiều cao của Text bên cạnh
                                    .padding(vertical = 2.dp) // Thụt vào 1 xíu ở 2 đầu cho tinh tế
                                    .background(BluePrimary.copy(alpha = 0.5f), CircleShape)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Text(
                                    text = "Example",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = BluePrimary,
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = word.example,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF64748B),
                                    fontStyle = FontStyle.Italic
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Nút Bookmark
                        IconButton(
                            onClick = onToggleSave,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = if (word.isSaved) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                                contentDescription = null,
                                tint = if (word.isSaved) BluePrimary else Color(0xFF94A3B8),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }

// Component PhoneticGroup (Giữ nguyên như cũ)

        @Composable
        fun RecentSearchList(items: List<String>) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) {
                items(items) { term ->
                    Surface(
                        onClick = {},
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.History,
                                null,
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                term,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF1F2937)
                            )
                        }
                    }
                }
            }
        }

        @Composable
        fun SectionTitle(title: String) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B),
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        @Composable
        fun EmptySearchResult(query: String) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 60.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.SearchOff,
                    null,
                    modifier = Modifier.size(80.dp),
                    tint = Color(0xFFCBD5E1)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "No results for \"$query\"",
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray,
                    fontSize = 16.sp
                )
                Text(
                    "Check your spelling or try another word.",
                    color = Color.Gray.copy(0.8f),
                    fontSize = 14.sp
                )
            }
        }
}