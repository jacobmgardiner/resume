import com.github.kittinunf.fuel.httpGet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class Movie(val movieData: RawMovieData) {
    val description = movieData.description
    val duration = movieData.duration
    var videoIdentifier = movieData.video_identifier
    var assetId = movieData.asset_id
    val title = movieData.cleanTitle()
    var imgUrl = movieData.img_url
    var category = movieData.category
    val platform = Platform.from(movieData.platform_id.toInt())
    var rating = Rating.from(if (movieData.rating_id.isEmpty()) -1 else movieData.rating_id.toInt())
    private val _tomatoerScore = MutableStateFlow(-2)
    val tomatoerScore = _tomatoerScore.asStateFlow()

    private fun movieSearchUrl(): String {
        return "https://www.google.com/search?q=${title.replace(" ", "+")}+site%3Arottentomatoes.com"
    }

    fun onVisible() {
        if (tomatoerScore.value > -2) return
        CoroutineScope(Dispatchers.Default).launch {
            _tomatoerScore.value = getTomatoerScore(getUrlFromName())
        }
    }

    private val userAgentKey = "User-Agent"
//    private val userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:92.0) Gecko/20100101 Firefox/92.0"
    private val userAgent = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.82 Mobile Safari/537.36"
    private fun getValidUrl(): String {
        var url = ""
        var response = ""
        try {
            response = movieSearchUrl().httpGet().appendHeader(userAgentKey, userAgent).response().toString()
        } catch (e: NoSuchMethodError) {
//            e.printStackTrace()
            println("ERROR SEARCHING GOOGLE: ${movieSearchUrl()}")
            return url
        }
        val match = """<a href="https://www.rottentomatoes.com/m/""".toRegex().find(response)
        if (match == null) {println("INVALID RESPONSE FOR: $title!");println(response);return url}

        val i = match.range.last
        val match2 = """" """.toRegex().find(response, i)
        val i2 = match2!!.range.first
        url = "https://www.rottentomatoes.com/m/${response.substring(i+1, i2)}"
        println("$title: $url")
        return url
    }

    private fun getUrlFromName(): String {
        return "https://www.rottentomatoes.com/m/${
            movieData.cleanTitle().lowercase()
//                .replace("the", "")
//                .replace("a", "")
                .stripPunctuation()
                .replace(" ", "_")
                .removePrefix(" ").removeSuffix(" ")
        }"
    }

    private fun getTomatoerScore(url: String): Int {
        val rtName = movieData.cleanTitle().lowercase().stripPunctuation().replace(" ", "_")
        var score = -1
//        val url = "https://www.rottentomatoes.com/m/$t"
        var str = ""
        try {
            str = url.httpGet().appendHeader(userAgentKey, userAgent).response().toString()
        } catch (e: NoSuchMethodError) {
//            e.printStackTrace()
            println("INVALID URL: $url")
            println(str)
            return getTomatoerScore(getValidUrl())
        }
        val match = """tomatometerscore="""".toRegex().find(str)
        if (match == null) {println("INVALID RESPONSE FOR: $rtName!");println(str);return score}

        val i = match.range.last
        val scoreStr = str.substring(i+1, i+3)
        println("$rtName: $scoreStr%")
        try {
            score = scoreStr.toInt()
        } catch (e: NumberFormatException) {
            println("INVALID SCORE!")
        }
        return score
    }

    val keywords = movieData.keywords()
    var searchScore = 0f
    fun setSearchScore(searchQuery: String) {
        val searchTerms = searchQuery.replace("\\p{Punct}".toPattern().toRegex(), "").split(" ")
        searchScore = 0f
        keywords.forEachIndexed { k, keyword ->
            if (keyword != null) {
                val weight = if (keyword.isArticle() || keyword.isCommon()) 1f else 50f
                if (searchTerms.contains(keyword)) {
                    searchScore += ((keywords.size - k) * weight) * 100
                } else {
                    searchScore -= ((keywords.size - k) * weight)
                }
            }
        }
    }
}

fun String.isArticle(): Boolean {
    return contentEquals("the", true) || contentEquals("a", true)
}

fun String.isCommon(): Boolean {
    return contentEquals("and", true)
}

enum class Rating(val string: String, val value: Int) {
    G("G", 0),//0
    PG("PG", 1),//1
    PG13("PG-13", 2),//2
    R("R", 3),//3
    NR("NR", -1);//""

    companion object {
        fun from(value: Int): Rating {
            for (i in 0 until values().size)
                if (values()[i].value == value)
                    return values()[i]
            return values()[if (value > 4) 4 else value]
        }
    }
}

enum class Platform(val string: String, val value: Int) {
    Amazon("Amazon", 7),//7
    Netflix("Netflix", 9),//9
    Disney("Disney+", 10),//10
    HBO("HBO", 11);//11

    companion object {
        fun from(value: Int): Platform {
            for (i in 0 until values().size)
                if (values()[i].value == value)
                    return values()[i]
            return values()[value]
        }
    }
}