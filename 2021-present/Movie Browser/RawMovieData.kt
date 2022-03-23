import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson
import java.util.*

data class RawMovieData(
    val asset_id: String = "15150",
    val asset_identifier_id: String = "22794",
    val category: String = "",
    val child: String? = null,
    val description: String = "",
    val duration: String = "",
    val format_id: String = "",
    val genre: String? = null,
    val identifiers: String = "",
    val img_url: String = "",
    val name: String = "",
    val number: String = "0",
    val platform_id: String = "7",
    val platform_title: String = "",
    val platforms: String = "",
    val provider_id: String = "3",
    val rating_id: String = "",
    val release_date: String = "",
    val type_id: String = "1",
    val video_identifier: String = "B006LMEDEK",
) {
    class Deserializer : ResponseDeserializable<Array<RawMovieData>> {
        override fun deserialize(content: String): Array<RawMovieData>
                = Gson().fromJson(content, Array<RawMovieData>::class.java)
    }

    fun keywords() = cleanTitle().stripPunctuation().lowercase(Locale.getDefault()).split(" ").orderByLength().take(Repository.Movies.getKeywords().size).toTypedArray().copyOf(Repository.Movies.getKeywords().size)

    fun cleanTitle(): String {
        var new = name
        if (new.contains(", The")) {
            new = new.replace(", The", "")
            new = "The $new"
        }
        if (new.contains(", A")) {
            new = new.replace(", A", "")
            new = "A $new"
        }
        new = new.replace("""\(.*\)""".toRegex(RegexOption.IGNORE_CASE), "")
        new = new.replace("""\(Disney.*""".toRegex(RegexOption.IGNORE_CASE), "")

        new = new.removeSuffix(" ")
        return new
    }
}

fun String.stripPunctuation(): String {
    return replace("\\p{Punct}".toPattern().toRegex(), "")
}

fun List<String>.orderByLength(): List<String> {
    val lengthComparator = Comparator { str1: String, str2: String -> str2.length - str1.length }
    println(sortedWith(lengthComparator))
    return sortedWith(lengthComparator)
}

