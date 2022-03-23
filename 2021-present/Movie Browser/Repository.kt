import androidx.compose.runtime.key
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.github.kittinunf.fuel.httpGet
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.skia.Image
import java.io.File
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import org.jetbrains.exposed.sql.transactions.transaction

object Repository {
    private const val FOLDER_NAME = "/cache/"
    private var images = hashMapOf<String, ImageBitmap>()
    private var directory = ""

    var storeInMemory = false
    var useCache = true

//    var platform = Platform.Amazon.value
    var platform = Platform.HBO.value
//    var platform = Platform.Netflix.value
//    var platform = 9

    init {
        val f = File(System.getProperty("user.dir") + FOLDER_NAME)
        if (!f.isDirectory) {
            if(f.mkdirs()) {
                directory = f.canonicalPath + "\\"
//                println(directory)
            }
        } else {
            directory = f.canonicalPath + "\\"
//            println(directory)
        }
    }

    private fun loadImageNetwork(movie: Movie): ImageBitmap {
//        println("downloading from: ${movie.imgUrl}")
        val (_, _, result) = movie.imgUrl.httpGet().response()
        val ba = result.get()
        val img = Image.makeFromEncoded(ba).asImageBitmap()
        if (useCache)
            storeImageDisk(movie, ba)
        if (storeInMemory)
            images[movie.imgUrl] = img
        return img
    }

    private fun storeImageDisk(movie: Movie, img: ByteArray) {
        CoroutineScope(Dispatchers.Default).launch {
            val f = File(directory + movie.assetId)
//            println("storing at: ${f.canonicalPath}")
            f.createNewFile()
            with(f.outputStream()) {
                write(img)
                close()
            }
        }
    }

    private fun loadImageDisk(movie: Movie): ImageBitmap {
        val f = File(directory + movie.assetId)
//        println("loading from: ${f.canonicalPath}")
        val ins = f.inputStream()
        val img = Image.makeFromEncoded(ins.readAllBytes()).asImageBitmap()
        ins.close()
        if (storeInMemory)
            images[movie.imgUrl] = img
        return img
    }

    fun inMemory(movie: Movie): Boolean {
        return storeInMemory && images[movie.imgUrl] != null
    }

    fun onDisk(movie: Movie): Boolean {
        return useCache && File(directory + movie.assetId).isFile
    }

    fun getImage(movie: Movie): ImageBitmap {
        return if (inMemory(movie)) {
            images[movie.imgUrl]!!
        } else if (onDisk(movie)) {
            loadImageDisk(movie)
        } else {
            loadImageNetwork(movie)
        }
    }

    fun downloadAndCacheImage(movie: Movie): Boolean {
        if (onDisk(movie)) return false
        loadImageNetwork(movie)
        return true
    }

    //2077 total
    private var loadSize = 500
    private var offset = 0

    private val movieListURL get() = "https://filter.clearplay.com/v1/search?platformId=$platform&offset=$offset&take=$loadSize"

    private var movies = arrayOf<RawMovieData>()

    fun updateMoviesList() {
        val (_, _, result) = movieListURL.httpGet().responseObject(RawMovieData.Deserializer())
        val data = result.get()
        storeMovies(data)
        if (data.size == loadSize) {
            offset += loadSize
            updateMoviesList()
        }
    }

    fun getMovies(startIdx: Int, endIdx: Int, callback: (Array<Movie>?) -> Unit) {
        var mq: Query? = null
        transaction {
            mq = Movies.select { Movies.order.greaterEq(startIdx) and Movies.order.lessEq(endIdx) }
            val arr = Array(mq?.count() ?: 0) {Movie(RawMovieData())}
            mq?.forEachIndexed { i, row -> arr[i] = Movie(Movies.toMovie(row)) }
            callback(arr)
        }
    }

    fun getMoviesByRating(offset: Int, loadSize: Int) {

    }

    fun searchMovies(query: String, callback: (Array<Movie>?) -> Unit) {
        //get list of all with matching keywords (maybe synonyms included)
        //give each score according to how many keywords are contained
        //adjust score according to percentage of keywords that are matching
        //order list according to scores
        val searchTerms = query.replace("\\p{Punct}".toPattern().toRegex(), "").split(" ")
        transaction {
            val mq = Movies.select {
                Movies.keyword0.inList(searchTerms) or
                        Movies.keyword1.inList(searchTerms) or
                        Movies.keyword2.inList(searchTerms) or
                        Movies.keyword3.inList(searchTerms) or
                        Movies.keyword4.inList(searchTerms) or
                        Movies.keyword5.inList(searchTerms) or
                        Movies.keyword6.inList(searchTerms) or
                        Movies.keyword7.inList(searchTerms) or
                        Movies.keyword8.inList(searchTerms) or
                        Movies.keyword9.inList(searchTerms)
            }
            val arr = Array(mq?.count() ?: 0) {Movie(RawMovieData())}
            mq?.forEachIndexed { i, row -> arr[i] = Movie(Movies.toMovie(row)) }
            callback(arr)
        }
    }

    private fun storeMovies(movies: Array<RawMovieData>) {
        //TODO("create keyword database")
        transaction {
            movies.forEachIndexed { idx, movie ->
                Movies.insertMovie(movie, idx, movie.keywords().toList()/*, getRating(getRottenTomatoesTitle(movie.cleanTitle()))*/)
            }
        }
    }

    init {
        Database.connect("jdbc:h2:./movies", driver = "org.h2.Driver")
        transaction {
            SchemaUtils.create(Movies)
        }
    }

    object Movies : Table() {
        fun insertMovie(movie: RawMovieData, num: Int, keywords: List<String?>/*, tScore: Int*/) {
            if (!Movies.select { Movies.asset_id.eq(movie.asset_id) }.empty()) return
            Movies.insert {
                it[asset_id] = movie.asset_id
                it[asset_identifier_id] = movie.asset_identifier_id
                it[category] = movie.category
                it[child] = movie.child ?: ""
                it[description] = movie.description
                it[duration] = movie.duration
                it[format_id] = movie.format_id
                it[genre] = movie.genre ?: ""
                it[identifiers] = movie.identifiers
                it[img_url] = movie.img_url
                it[name] = movie.name
                it[number] = movie.number
                it[platform_id] = movie.platform_id
                it[platform_title] = movie.platform_title
                it[platforms] = movie.platforms
                it[provider_id] = movie.provider_id
                it[rating_id] = movie.rating_id
                it[release_date] = movie.release_date
                it[type_id] = movie.type_id
                it[video_identifier] = movie.video_identifier
                it[order] = num
//                it[tomatoerScore] = tScore

                getKeywords().forEachIndexed { i, k ->
                    it[k] = keywords[i] ?: ""
                }
            }
        }

        fun toMovie(movieRow: ResultRow): RawMovieData {
            return RawMovieData(
                movieRow[asset_id],
                movieRow[asset_identifier_id],
                movieRow[category],
                movieRow[child],
                movieRow[description],
                movieRow[duration],
                movieRow[format_id],
                movieRow[genre],
                movieRow[identifiers],
                movieRow[img_url],
                movieRow[name],
                movieRow[number],
                movieRow[platform_id],
                movieRow[platform_title],
                movieRow[platforms],
                movieRow[provider_id],
                movieRow[rating_id],
                movieRow[release_date],
                movieRow[type_id],
                movieRow[video_identifier],
            )
        }

        fun getKeywords(): Array<Column<String>> {
            return arrayOf(
                keyword0,
                keyword1,
                keyword2,
                keyword3,
                keyword4,
                keyword5,
                keyword6,
                keyword7,
                keyword8,
                keyword9,
            )
        }

        val keyword0 = varchar("keyword0", 50)
        val keyword1 = varchar("keyword1", 50)
        val keyword2 = varchar("keyword2", 50)
        val keyword3 = varchar("keyword3", 50)
        val keyword4 = varchar("keyword4", 50)
        val keyword5 = varchar("keyword5", 50)
        val keyword6 = varchar("keyword6", 50)
        val keyword7 = varchar("keyword7", 50)
        val keyword8 = varchar("keyword8", 50)
        val keyword9 = varchar("keyword9", 50)

        val asset_id = varchar("asset_id", 50).primaryKey()
        val asset_identifier_id = varchar("asset_identifier_id", 50)
        val category = varchar("category", 50)
        val child = varchar("child", 50)
        val description = varchar("description", 5000)
        val duration = varchar("duration", 50)
        val format_id = varchar("format_id", 50)
        val genre = varchar("genre", 50)
        val identifiers = varchar("identifiers", 50)
        val img_url = varchar("img_url", 250)
        val name = varchar("name", 50)
        val number = varchar("number", 50)
        val platform_id = varchar("platform_id", 50)
        val platform_title = varchar("platform_title", 150)
        val platforms = varchar("platforms", 50)
        val provider_id = varchar("provider_id", 50)
        val rating_id = varchar("rating_id", 50)
        val release_date = varchar("release_date", 50)
        val type_id = varchar("type_id", 50)
        val video_identifier = varchar("video_identifier", 50)
        val order = integer("order")
//        val tomatoerScore = integer("tomatoerScore")
    }
}