package com.codework.daily_english.presentation.screen.dictionary

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.codework.daily_english.presentation.components.BaseScreen
import com.codework.daily_english.presentation.model.WordItem
import com.codework.daily_english.presentation.theme.AppBackground

@Composable
fun DictionaryScreen(
    viewModel: DictionaryViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Color.White,
        topBar = {
            DictionaryTopBar(onBackClick = onBackClick)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            BaseScreen(state = state) { data ->
                Column(modifier = Modifier.fillMaxSize()) {
                    // 1. THANH TÌM KIẾM
                    DictionarySearchBar(
                        query = data.query,
                        onQueryChange = { viewModel.onEvent(DictionaryEvent.OnSearchQueryChange(it)) },
                        onClear = { viewModel.onEvent(DictionaryEvent.OnClearSearch) }
                    )

                    // 2. NỘI DUNG CHÍNH
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (data.query.isNotEmpty()) {
                            if (data.searchResults.isEmpty()) {
                                item { EmptySearchResult(data.query) }
                            } else {
                                items(data.searchResults) { word ->
                                    WordResultCard(
                                        word = word,
                                        onPlayAudioUk = { viewModel.onEvent(DictionaryEvent.OnPlayAudio(word.word)) },
                                        onPlayAudioUs = { viewModel.onEvent(DictionaryEvent.OnPlayAudio(word.word)) },
                                        onToggleSave = { /* Logic bookmark */ }
                                    )
                                }
                            }
                        } else {
                            item {
                                SectionTitle("Word of the Day 🌟")
                                Spacer(modifier = Modifier.height(8.dp))
                                WordOfTheDayCardPremium(
                                    word = data.wordOfTheDay,
                                    onPlayAudio = { viewModel.onEvent(DictionaryEvent.OnPlayAudio(data.wordOfTheDay.word)) }
                                )
                            }

                            item {
                                Spacer(modifier = Modifier.height(12.dp))
                                SectionTitle("Recent Searches")
                                Spacer(modifier = Modifier.height(8.dp))
                                RecentSearchList(data.recentSearches)
                            }
                        }
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryTopBar(onBackClick: () -> Unit) {
    Surface(color = Color.White, modifier = Modifier.fillMaxWidth()) {
        Column {
            TopAppBar(
                windowInsets = WindowInsets(0),
                modifier = Modifier.height(72.dp),
                title = {
                    Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.CenterStart) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(42.dp).border(1.dp, Color(0xFFF3F4F6), CircleShape).padding(3.dp)) {
                                Box(modifier = Modifier.fillMaxSize().background(Brush.linearGradient(listOf(Color(0xFF4FACFE), Color(0xFF8E2DE2))), CircleShape), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.AutoAwesome, null, tint = Color.White, modifier = Modifier.size(16.dp))
                                }
                                Box(modifier = Modifier.align(Alignment.BottomEnd).size(10.dp).background(Color.White, CircleShape).padding(1.5.dp)) {
                                    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF4CAF50), CircleShape))
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Gemini Tutor", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color(0xFF111827))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("Always active", fontSize = 11.sp, color = Color.Gray)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(color = Color(0xFFEFF6FF), shape = RoundedCornerShape(4.dp)) {
                                        Text("PRO", modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp), fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3B82F6))
                                    }
                                }
                            }
                        }
                    }
                },
                navigationIcon = {
                    Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                        IconButton(onClick = onBackClick, modifier = Modifier.padding(start = 8.dp).size(36.dp).background(Color(0xFFF2F4F7), CircleShape)) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.Black, modifier = Modifier.size(18.dp))
                        }
                    }
                },
                actions = {
                    Box(modifier = Modifier.fillMaxHeight(), contentAlignment = Alignment.Center) {
                        IconButton(onClick = { }, modifier = Modifier.padding(end = 8.dp).size(36.dp).background(Color(0xFFF2F4F7), CircleShape)) {
                            Icon(Icons.Default.Menu, null, tint = Color.Black, modifier = Modifier.size(18.dp))
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray.copy(alpha = 0.3f))
        }
    }
}

@Composable
fun DictionarySearchBar(query: String, onQueryChange: (String) -> Unit, onClear: () -> Unit) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth().height(48.dp).background(Color(0xFFF2F4F7), RoundedCornerShape(12.dp)).padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Search, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(10.dp))
            androidx.compose.foundation.text.BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.weight(1f),
                textStyle = MaterialTheme.typography.bodyLarge,
                singleLine = true,
                decorationBox = { innerTextField ->
                    if (query.isEmpty()) Text("Search words, idioms...", color = Color.Gray, fontSize = 15.sp)
                    innerTextField()
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
    word: WordItem,
    onPlayAudioUk: () -> Unit,
    onPlayAudioUs: () -> Unit,
    onToggleSave: () -> Unit
) {
    Card(
        // SỬA: Dùng nền trắng để Card nổi bật trên nền màn hình xanh nhạt
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F9FF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = word.word, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(color = Color(0xFFF1F5F9), shape = RoundedCornerShape(6.dp)) {
                        Text(text = word.type, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), style = MaterialTheme.typography.labelSmall, color = Color(0xFF475569), fontStyle = FontStyle.Italic)
                    }
                }

                // NÚT BOOKMARK: Đã bỏ viền và đổi màu linh hoạt
                IconButton(
                    onClick = onToggleSave,
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = if (word.isSaved) Color(0xFF2563EB) else Color(0xFFF1F5F9),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = if (word.isSaved) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                        contentDescription = null,
                        tint = if (word.isSaved) Color.White else Color(0xFF64748B),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                PhoneticSimpleItem(label = "UK", phonetic = word.phoneticUk, color = Color(0xFF2563EB), onClick = onPlayAudioUk)
                Spacer(modifier = Modifier.width(16.dp))
                PhoneticSimpleItem(label = "US", phonetic = word.phoneticUs, color = Color(0xFFDC2626), onClick = onPlayAudioUs)
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp, color = Color(0xFFE2E8F0))

            Text(text = word.definition, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF334155), lineHeight = 22.sp)

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Ex: ",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B),
                    fontSize = 13.sp,
                    modifier = Modifier.alignByBaseline()
                )
                Text(
                    text = word.example,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF64748B),
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.alignByBaseline()
                )
            }
        }
    }
}

@Composable
fun PhoneticSimpleItem(label: String, phonetic: String, color: Color, onClick: () -> Unit) {
    Row(
        modifier = Modifier.clip(RoundedCornerShape(4.dp)).clickable { onClick() }.padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.VolumeUp, null, tint = color, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("$label $phonetic", fontSize = 13.sp, color = Color.DarkGray, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun WordOfTheDayCardPremium(word: WordItem, onPlayAudio: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.background(Brush.linearGradient(listOf(Color(0xFF4FACFE), Color(0xFF8E2DE2))))) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = Color.White.copy(0.2f), shape = RoundedCornerShape(6.dp)) {
                        Text("WORD OF THE DAY", modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp), color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    // Phiên âm để cạnh label
                    Text("UK: ${word.phoneticUk}  US: ${word.phoneticUs}", color = Color.White.copy(0.85f), fontSize = 11.sp, fontStyle = FontStyle.Italic)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(word.word, style = MaterialTheme.typography.headlineSmall, color = Color.White, fontWeight = FontWeight.Black)
                Spacer(modifier = Modifier.height(4.dp))
                Text(word.definition, color = Color.White, lineHeight = 20.sp, maxLines = 2, overflow = TextOverflow.Ellipsis, fontSize = 14.sp)

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onPlayAudio,
                    modifier = Modifier.height(36.dp).fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(10.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Default.VolumeUp, null, tint = Color(0xFF8E2DE2), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Listen", color = Color(0xFF8E2DE2), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun RecentSearchList(items: List<String>) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items) { term ->
            Surface(
                onClick = {},
                shape = RoundedCornerShape(10.dp),
                color = Color.White,
                border = BorderStroke(1.dp, Color(0xFFF1F5F9))
            ) {
                Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.History, null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(term, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black, color = Color(0xFF1E293B))
}

@Composable
fun EmptySearchResult(query: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.Search, null, modifier = Modifier.size(64.dp), tint = Color(0xFFF1F5F9))
        Spacer(modifier = Modifier.height(12.dp))
        Text("No results for \"$query\"", fontWeight = FontWeight.Bold, color = Color.Gray, fontSize = 14.sp)
    }
}