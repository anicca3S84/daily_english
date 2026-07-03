    package com.codework.dailyenglish.presentation.screen.practice

    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.lazy.LazyColumn
    import androidx.compose.foundation.lazy.LazyRow
    import androidx.compose.foundation.lazy.items
    import androidx.compose.foundation.shape.CircleShape
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.automirrored.filled.ArrowForward
    import androidx.compose.material.icons.filled.*
    import androidx.compose.material.icons.rounded.EmojiEvents
    import androidx.compose.material3.*
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Brush
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.graphics.vector.ImageVector
    import androidx.compose.ui.text.font.FontWeight
    import androidx.compose.ui.unit.dp
    import com.codework.dailyenglish.domain.vocabulary.model.VocabTopic
    import com.codework.dailyenglish.presentation.components.CurvedHeaderWrapper
    import com.codework.dailyenglish.presentation.components.StateWrapper
    import com.codework.dailyenglish.presentation.screen.practice.components.AiTopicCard
    import com.codework.dailyenglish.presentation.screen.practice.components.GameCardPro
    import com.codework.dailyenglish.presentation.screen.practice.components.MoreTopicCard
    import com.codework.dailyenglish.presentation.screen.practice.components.PaddingSectionTitle
    import com.codework.dailyenglish.presentation.screen.practice.model.GameItem
    import com.codework.dailyenglish.presentation.screen.practice.mvi.PracticeEvent
    import com.codework.dailyenglish.presentation.screen.practice.mvi.PracticeState

    @Composable
    fun PracticeScreen(
        state: PracticeState,
        onEvent: (PracticeEvent) -> Unit
    ) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surface),
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // 1. Header & Stats
                item {
                    CurvedHeaderWrapper(
                        content = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            ) {
                                Text(
                                    text = "Practice Arena",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.weight(1f)
                                )
                                Surface(
                                    color = Color.White.copy(alpha = 0.2f),
                                    shape = CircleShape
                                ) {
                                    IconButton(onClick = { onEvent(PracticeEvent.OnClickLeaderboard) }) {
                                        Icon(
                                            imageVector = Icons.Rounded.EmojiEvents,
                                            contentDescription = "Leaderboard",
                                            tint = Color.White
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "Let's boost your skills!",
                                color = Color.White.copy(0.8f),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(horizontal = 24.dp)
                            )
                        },
                        bottomCardContent = {
    //                        HeaderStatItem(
    //                            value = "${data.totalPoints}",
    //                            label = "Total XP",
    //                            icon = Icons.Default.EmojiEvents,
    //                            iconColor = Color(0xFFFFC107)
    //                        )
                            Divider(modifier = Modifier.height(40.dp).width(1.dp), color = Color.LightGray)
    //                        HeaderStatItem(
    //                            value = "${data.streak}",
    //                            label = "Day Streak",
    //                            icon = Icons.Default.LocalFireDepartment,
    //                            iconColor = Color(0xFFFF5722)
    //                        )
                        }
                    )
                }

                item {
                    Column {
                        PaddingSectionTitle(title = "AI Speaking Hub 🤖", subtitle = "Choose a topic and start talking")
                        Spacer(modifier = Modifier.height(16.dp))

                        StateWrapper(uiState = state.topicsState) { topics ->
                            val allTopics = topics ?: emptyList()
                            val displayList = allTopics.take(5) // Lấy tối đa 7 item

                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // 1. Nút tĩnh: Free Talk
                                item {
                                    AiTopicCard(
                                        topic = VocabTopic(
                                            id = "free_talk",
                                            name = "Free Talk",
                                            imageUrl = "https://cdn-icons-png.flaticon.com/512/2040/2040946.png",
                                            description = "Explore this topic",
                                            totalWords = 0
                                        ),
                                        onClick = { onEvent(PracticeEvent.OnAiTopicClick("free_talk", "Free Talk")) }
                                    )
                                }

                                // 2. Render tối đa 7 chủ đề từ API
                                items(displayList) { topic ->
                                    AiTopicCard(
                                        topic = topic,
                                        onClick = { onEvent(PracticeEvent.OnAiTopicClick(topic.id, topic.name)) }
                                    )
                                }

                                // 3. Nút "Load More" (Chỉ hiện nếu có nhiều hơn 7 chủ đề)
                                if (allTopics.size > 5) {
                                    item {
                                        MoreTopicCard(
                                            onClick = { onEvent(PracticeEvent.OnExploreMoreAiTopics) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                item {
                    PaddingSectionTitle(title = "Game Arena 🎮", subtitle = "Fun way to master skills")
                }

                val games = listOf(
                    GameItem("Word Match", "Vocabulary", Icons.Default.Extension, Color(0xFFFF9800)),
                    GameItem("Speed Quiz", "Grammar", Icons.Default.Timer, Color(0xFF4CAF50)),
                    GameItem("Listening Hero", "Listening", Icons.Default.Headset, Color(0xFF2196F3)),
                    GameItem("Sentence Builder", "Writing", Icons.Default.Build, Color(0xFF9C27B0))
                )

                items(games.chunked(2)) { pair ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        repeat(pair.count()) { index ->
                            val game = pair[index]
                            GameCardPro(
                                game = game,
                                modifier = Modifier.weight(1f),
                                onClick = { onEvent(PracticeEvent.OnGameClick(game.name)) } // Nối Event
                            )
                        }
                        if (pair.count() < 2) Spacer(modifier = Modifier.weight(1f))
                    }
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }
            }
    }
