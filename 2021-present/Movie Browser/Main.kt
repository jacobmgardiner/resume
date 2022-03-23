// Copyright 2000-2021 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
import androidx.compose.animation.*
import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.awt.Desktop
import java.net.URI
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import java.awt.event.KeyEvent

private var loadSize = 50
private var offset = 0

private var _movies: MutableStateFlow<Array<Movie>> = MutableStateFlow(arrayOf())
var movies = _movies.asStateFlow()

private fun amazonUrl(id: String) = "https://www.amazon.com/gp/video/detail/$id"
private fun netflixUrl(id: String) = ""
private fun hboUrl(id: String) = "https://play.hbomax.com/page/urn:hbo:page:$id:type:feature"
private fun disneyUrl(id: String) = ""

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun App() {
    DesktopMaterialTheme {
        Box(contentAlignment = Alignment.BottomCenter) {
            Column {
                Spacer(Modifier.height(8.dp))

                Platforms()

                Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Filters()
                    Spacer(Modifier.weight(1f))
                    Search()
                }
//                Spacer(Modifier.height(8.dp))
                val moviesState = movies.collectAsState(arrayOf())
                MovieList(moviesState.value)

            }

            lastHoveredMovie.collectAsState().value?.let { DetailsPanel(it) }
//            lastSelectedMovie.collectAsState().value?.let { DetailsPanel(it) }
        }
    }
}

@Composable
fun Platforms() {
    Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically)  {
        Text("Platform", fontWeight = FontWeight.Bold)
        Button({ Repository.platform = Platform.Amazon.value }, Modifier.padding(8.dp)) {
            Text("Amazon")
        }
        Button({ Repository.platform = Platform.HBO.value }, Modifier.padding(8.dp)) {
            Text("HBO")
        }
        Button({ Repository.platform = Platform.Netflix.value }/*, Modifier.padding(8.dp)*/) {
            Text("Netflix")
        }
    }
}

var _lastHoveredMovie: MutableStateFlow<Movie?> = MutableStateFlow(null)
var lastHoveredMovie = _lastHoveredMovie.asStateFlow()

var _lastSelectedMovie: MutableStateFlow<Movie?> = MutableStateFlow(null)
var lastSelectedMovie = _lastSelectedMovie.asStateFlow()

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DetailsPanel(movie: Movie) {
    Surface(
        Modifier.fillMaxWidth()
            .fillMaxHeight(0.25f).pointerMoveFilter(
                onEnter = {
                    _lastHoveredMovie.value = lastSelectedMovie.value
                    false
                },
                onExit = {
//            _lastSelectedMovie.value = null
                    false
                }),
        color = Color.Gray.copy(0.8f),
    ) {
        Row {
            Column(Modifier.padding(8.dp).fillMaxWidth(0.75f)) {
                Text(movie.title, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(movie.rating.string, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
//                Text("${movie.tomatoerScore}%", color = Color.White, fontWeight = FontWeight.Bold)
//                Spacer(Modifier.height(4.dp))
                Text("Category: ${movie.category}", color = Color.White)
                Spacer(Modifier.height(4.dp))
                Text(movie.description, color = Color.White)
            }
//            Spacer(Modifier.weight(1f))
            if (lastHoveredMovie.collectAsState().value == lastSelectedMovie.collectAsState().value)
                Actions(movie)
        }
    }
}

@Composable
fun Actions(movie: Movie) {
    Column(Modifier.padding(8.dp).fillMaxWidth()) {
        Button(
            {
                openWebpage(
                    when (movie.platform) {
                        Platform.Amazon -> amazonUrl(movie.videoIdentifier)
                        Platform.HBO -> hboUrl(movie.videoIdentifier)
                        Platform.Netflix -> netflixUrl(movie.videoIdentifier)
                        Platform.Disney -> disneyUrl(movie.videoIdentifier)
                        else -> amazonUrl(movie.videoIdentifier)
                    }
            ) },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray.copy(0.75f)),
        ) {
            Text("Open in Browser", color = Color.White)
        }
        Spacer(Modifier.height(4.dp))
        Button(
            onClick = { openWebpage("https://www.google.com/search?q=${movie.title.replace(" ", "+")}+site%3Arottentomatoes.com") },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.DarkGray.copy(0.75f)),
        ) {
            Text("Search for ratings", color = Color.White)
        }
        Spacer(Modifier.height(4.dp))
        Lists(movie)
    }
}

@Composable
fun Lists(movie: Movie) {
    Text("Lists", color = Color.White)
//    lists.forEach {
//        Button({}) {
//            Text(it.name)
//        }
//    }
}

@Composable
fun Search() {
    Row(Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
        var text by remember { mutableStateOf(TextFieldValue()) }
        TextField(
            label = {Text("search movies")},
            value = text,
            onValueChange = { text = it },
            modifier = Modifier.onKeyEvent {
                if (it.nativeKeyEvent.keyCode == KeyEvent.VK_ENTER){
                    search(text.text)
                }
                true
            },
            maxLines = 1,
        )
        Spacer(Modifier.width(4.dp))
        Button({ search(text.text) }) {
            Text("Search")
        }
    }
}

fun search(value: String) {
    offset = 0
    Repository.searchMovies(value) { arr ->
        if (arr == null) return@searchMovies
        arr.forEach { movie ->
            movie.setSearchScore(value)
        }
        arr.sortWith(Comparator { mov1: Movie, mov2: Movie -> (mov2.searchScore - mov1.searchScore).toInt() })
        _movies.value = arr
    }
}

val _ratingFilters: MutableStateFlow<Array<Boolean>> = MutableStateFlow(Array(5) {true})
val ratingFilters = _ratingFilters.asStateFlow()

@Composable
fun Filters(/*ratingFilters: Array<Int>*/) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("Filters ", fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(8.dp))
        Text("Rating")
        Spacer(Modifier.width(4.dp))
        Text("R")
        var r by remember { mutableStateOf(true) }
        Checkbox(checked = r, { r = it;_ratingFilters.value[Rating.R.ordinal] = r;_ratingFilters.value = _ratingFilters.value })
        Text("PG-13")
        var pg13 by remember { mutableStateOf(true) }
        Checkbox(checked = pg13, { pg13 = it;_ratingFilters.value[Rating.PG13.ordinal] = pg13;_ratingFilters.value = _ratingFilters.value })
        Text("PG")
        var pg by remember { mutableStateOf(true) }
        Checkbox(checked = pg, { pg = it;_ratingFilters.value[Rating.PG.ordinal] = pg;_ratingFilters.value = _ratingFilters.value })
        Text("G")
        var g by remember { mutableStateOf(true) }
        Checkbox(checked = g, { g = it;_ratingFilters.value[Rating.G.ordinal] = g;_ratingFilters.value = _ratingFilters.value })
        Text("NR")
        var nr by remember { mutableStateOf(true) }
        Checkbox(checked = nr, { nr = it;_ratingFilters.value[Rating.NR.ordinal] = nr;_ratingFilters.value = _ratingFilters.value })
    }
}

fun applyFilters(ratingFilters: Array<Boolean>, movies: Array<Movie>): List<Movie> {
    val filtered = arrayListOf<Movie>()
    movies.forEach { if (ratingFilters[it.rating.ordinal]) { filtered.add(it) } }
    return filtered.toList()
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieList(movies: Array<Movie>) {
    //TODO
    val filtered = applyFilters(ratingFilters.collectAsState().value, movies)
    val listState = rememberLazyListState()
    LazyVerticalGrid(
        state = listState,
        cells = GridCells.Adaptive(minSize = 128.dp),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalArrangement = Arrangement.Center,
    ) {

//        items(movies.size) {
        items(filtered.size) {
//            if (ratingFilters.collectAsState().value[movies[it].rating.ordinal]) {
            MovieButton(filtered[it])
//            }
        }

        item {
            Button({ onLoadMoreMovies() }, Modifier.fillMaxSize()) {
                Text("Load More")
            }
        }
        for (i in 1..10) {
            item {
                Surface(Modifier.height(190.dp)) {  }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun MovieButton(movie: Movie) {
//    if (!filters[movie.rating.ordinal]) return
//    var overlayVisible by remember { mutableStateOf(false) }
    Box(Modifier.padding(4.dp).pointerMoveFilter(
        onEnter = {
//            overlayVisible = true
            _lastHoveredMovie.value = movie
            false
        },
        onExit = {
//            overlayVisible = false
//            _lastSelectedMovie.value = null
            false
        }).clickable { _lastSelectedMovie.value = movie }
        .border(width = 8.dp, color = if (movie == _lastSelectedMovie.collectAsState().value) Color.Red else Color.Transparent)
    ) {
        Image(
            bitmap = Repository.getImage(movie),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
        )
        Score(movie)

//        AnimatedVisibility(modifier = Modifier.fillMaxSize(), visible = overlayVisible,
//            enter = fadeIn(),
//            exit = fadeOut()
//        ) {
//            Overlay(movie)
//        }
    }
}

@Composable
fun Score(movie: Movie) {
    movie.onVisible()
    val score = movie.tomatoerScore.collectAsState().value
    Surface(modifier = Modifier.padding(4.dp), color = Color.Red.copy(0.8f)) {
        if (score < 0)
            CircularProgressIndicator(color = Color.White)
        else
            Text("${if (score < 0) "error" else score}%", color = Color.White, fontWeight = FontWeight.Bold)
    }
}

fun openWebpage(url: String): Boolean {
    val desktop = if (Desktop.isDesktopSupported()) Desktop.getDesktop() else null
    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
        try {
            desktop.browse(URI(url))
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return false
}

fun main() = application {
    Repository.updateMoviesList()
    onLoadMoreMovies()
    Window(title = "Clearplay Browser", onCloseRequest = ::exitApplication) {
        App()
    }
}

fun onLoadMoreMovies() {
    CoroutineScope(Dispatchers.Default).launch {
        Repository.getMovies(offset, offset + loadSize) {
            println("loaded movies")
            _movies.value += it!!
            println("added loaded movies to list")
            offset += loadSize
            CoroutineScope(Dispatchers.Default).launch {
                downloadImages(it)
            }
        }
    }
}

fun downloadImages(newMovies: Array<Movie>) {
    newMovies.forEach { Repository.downloadAndCacheImage(it) }
}