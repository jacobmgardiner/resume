package com.yoloapps.location.utils

import android.util.Log
import com.google.firebase.firestore.GeoPoint
import kotlin.math.ceil
import kotlin.math.pow

/**
 * A collection of geohash utils.
 * The precision used here is 6 characters.
 *
 * A geohash is a rectangular location encoded as a string of characters. It can allow for a query of firebase docs roughly sorted by distance.
 *
 * [Learn more](https://en.wikipedia.org/wiki/Geohash)
 */
object Geohash {
    /**
     * The precision of the geohashes used in this object.
     */
    const val GEOHASH_PRECISION = 6
    /**
     * The number of 6 character geohashes along the x or y axis.
     */
    private const val MAX_COMPONENT = 32767
    /**
     * The longitude covered by a 6 character geohash
     */
    const val LON_PER_GEOHASH = 360 / MAX_COMPONENT.toDouble()
    /**
     * The latitude covered by a 6 character geohash
     */
    const val LAT_PER_GEOHASH = 180 / MAX_COMPONENT.toDouble()
    const val BASE32 = "0123456789bcdefghjkmnpqrstuvwxyz"

    /**
     * Returns a 6 character geohash that contains the given coordinates.
     */
    fun geohash(lat: Double, lon: Double): String {
        val precision =
            GEOHASH_PRECISION

        var idx = 0 // index into base32 map
        var bit = 0 // each char holds 5 bits
        var evenBit = true
        var geohash = ""

        var latMin: Double =  -90.0
        var latMax: Double =  90.0
        var lonMin: Double = -180.0
        var lonMax: Double = 180.0

        while (geohash.length < precision) {
            if (evenBit) {
                // bisect E-W longitude
                val lonMid = (lonMin + lonMax) / 2
                if (lon >= lonMid) {
                    idx = idx*2 + 1
                    lonMin = lonMid
                } else {
                    idx *= 2
                    lonMax = lonMid
                }
            } else {
                // bisect N-S latitude
                val latMid = (latMin + latMax) / 2
                if (lat >= latMid) {
                    idx = idx*2 + 1
                    latMin = latMid
                } else {
                    idx *= 2
                    latMax = latMid
                }
            }
            evenBit = !evenBit

            if (++bit == 5) {
                // 5 bits gives us a character: append it and start over
                geohash += BASE32[idx]

                bit = 0
                idx = 0

            }
        }

        return geohash;
    }

    /**
     * Returns a geohash of given precision that contains the given coordinates.
     */
    fun geohash(lat: Double, lon: Double, precision: Int): String {

        var idx = 0 // index into base32 map
        var bit = 0 // each char holds 5 bits
        var evenBit = true
        var geohash = ""

        var latMin: Double =  -90.0
        var latMax: Double =  90.0
        var lonMin: Double = -180.0
        var lonMax: Double = 180.0

        while (geohash.length < precision) {
            if (evenBit) {
                // bisect E-W longitude
                val lonMid = (lonMin + lonMax) / 2
                if (lon >= lonMid) {
                    idx = idx*2 + 1
                    lonMin = lonMid
                } else {
                    idx *= 2
                    lonMax = lonMid
                }
            } else {
                // bisect N-S latitude
                val latMid = (latMin + latMax) / 2
                if (lat >= latMid) {
                    idx = idx*2 + 1
                    latMin = latMid
                } else {
                    idx *= 2
                    latMax = latMid
                }
            }
            evenBit = !evenBit

            if (++bit == 5) {
                // 5 bits gives us a character: append it and start over
                geohash += BASE32[idx]

                bit = 0
                idx = 0

            }
        }

        return geohash;
    }

    /**
     * Returns all the geohashes within a given radius from a given point.
     * @param [lat] the latitude of the center point
     * @param [lon] the longitude of the center point
     * @param [distance] the "radius" of the grid of geohashes
     * @param [callback] called when done and passed the list of geohashes
     */
    fun surroundingGeohashes(lat: Double, lon: Double, distance: Int, callback: (List<String>) -> Unit) {
        val centerGeohash =
            geohash(
                lat,
                lon
            )
        Log.d("XXXXXXXXXX", "CENTER GEOHASH: " + centerGeohash)
        Log.d(
            "XXXXXXXXXX",
            "BINARY: " + binToString(
                geohashToBin(
                    centerGeohash
                )
            )
        )
        val gs = Array<String>(
            numberOfGeohashes(
                distance
            )
        ) { "" }
        val c = getComponents(
            centerGeohash
        )
        var xi = c[0]
        var yi = c[1]
        var x = 0
        var y = 0
        Log.d("XXXXXXXXXX", "GEOHASH COMPONENTS: " + c[0] + ", " + c[1])
        var i = 0
        gs[i++] =
            componentsToGeohash(
                xi,
                yi
            )
        while (i < gs.size) {
            y++
            x = 0
            while (i < gs.size && x <= y) {
                if (x == 0) {
                    gs[i++] =
                        componentsToGeohash(
                            xi,
                            yi + y
                        )
                    if (i >= gs.size)
                        break
                    gs[i++] =
                        componentsToGeohash(
                            xi,
                            yi - y
                        )
                    if (i >= gs.size)
                        break
                    gs[i++] =
                        componentsToGeohash(
                            xi + y,
                            yi
                        )
                    if (i >= gs.size)
                        break
                    gs[i++] =
                        componentsToGeohash(
                            xi - y,
                            yi
                        )
                } else if (y == x) {
                    gs[i++] =
                        componentsToGeohash(
                            xi + x,
                            yi + y
                        )
                    if (i >= gs.size)
                        break
                    gs[i++] =
                        componentsToGeohash(
                            xi - x,
                            yi + y
                        )
                    if (i >= gs.size)
                        break
                    gs[i++] =
                        componentsToGeohash(
                            xi + x,
                            yi - y
                        )
                    if (i >= gs.size)
                        break
                    gs[i++] =
                        componentsToGeohash(
                            xi - x,
                            yi - y
                        )
                } else {
                    gs[i++] =
                        componentsToGeohash(
                            xi + x,
                            yi + y
                        )
                    if (i >= gs.size)
                        break
                    gs[i++] =
                        componentsToGeohash(
                            xi - x,
                            yi + y
                        )
                    if (i >= gs.size)
                        break
                    gs[i++] =
                        componentsToGeohash(
                            xi + x,
                            yi - y
                        )
                    if (i >= gs.size)
                        break
                    gs[i++] =
                        componentsToGeohash(
                            xi - x,
                            yi - y
                        )
                    if (i >= gs.size)
                        break
                    gs[i++] =
                        componentsToGeohash(
                            xi + y,
                            yi + x
                        )
                    if (i >= gs.size)
                        break
                    gs[i++] =
                        componentsToGeohash(
                            xi - y,
                            yi + x
                        )
                    if (i >= gs.size)
                        break
                    gs[i++] =
                        componentsToGeohash(
                            xi + y,
                            yi - x
                        )
                    if (i >= gs.size)
                        break
                    gs[i++] =
                        componentsToGeohash(
                            xi - y,
                            yi - x
                        )
                }
                x++
            }
        }
        for (g in gs)
            Log.d("XXXXXXXXXX", "GEOHASHES: " + g)
        callback(gs.toList())
    }

    /**
     * Used by [surroundingGeohashes] to determine how many geohashes to find based on the given distance.
     */
    private fun numberOfGeohashes(dis: Int): Int {
        return if(dis <= 0)
            1
        else
            numberOfGeohashes(dis - 1) + 4 * (2 * dis)
    }

    /**
     * Splits the given geohash into coordinates.
     * Each coordinate is a 6 character geohash.
     * This means that the range is [0-32767] or [MAX_COMPONENT]
     */
    fun getComponents(g: String): List<Int> {
        val bin = geohashToBin(g)
        val lng = ArrayList<Int>()
        val lat = ArrayList<Int>()
        for((i, b) in bin.withIndex()) {
            if(i % 2 == 0) {
                lng.add(b)
            } else {
                lat.add(b)
            }
        }
        return listOf(
            binToDec(lat.toIntArray()),
            binToDec(lng.toIntArray())
        )
    }

    /**
     * Turns geohash components into a geohash.
     */
    fun componentsToGeohash(lat: Int, lng: Int): String {
        val lngbin = decToBin(
            lng,
            ceil((GEOHASH_PRECISION * 5 / 2).toDouble()).toInt()
        )
        val latbin = decToBin(
            lat,
            GEOHASH_PRECISION * 5 / 2
        )
        val bin = IntArray(lngbin.size + latbin.size)
        var lnginit = 0
        var latinit = 0
        for((i, b) in bin.withIndex()) {
            if(i % 2 == 0) {
                bin[i] = lngbin[lnginit++]
            } else {
                bin[i] = latbin[latinit++]
            }
        }
        return binToGeohash(bin)
    }

    /**
     * Turns a geohash into an base ten Int.
     */
    fun geohashToDec(g: String): Int {
        return binToDec(
            geohashToBin(g)
        )
    }

    /**
     * Turns a geohash into its binary form.
     */
    private fun geohashToBin(g: String): IntArray {
        val bin = IntArray(g.length*5)
        for ((i, char) in g.withIndex()) {
            val new = decToBin(
                BASE32.indexOf(char),
                5
            )
            for((j, n) in new.withIndex()) {
                bin[i*5+j] = n
            }
        }
        return bin
    }

    /**
     * Turns binary into a geohash.
     */
    private fun binToGeohash(binary: IntArray): String {
        var geo = ""
        val bs = binary.withIndex()
            .groupBy { it.index / 5 }
            .map { it.value.map { it.value } }
        for((i, b) in bs.withIndex()) {
            geo += BASE32[binToDec(
                b.toIntArray()
            )]
        }
        return geo
    }

    /**
     * Turns base ten to base two.
     */
    private fun decToBin(dec: Int): IntArray {
        val bin = ArrayList<Int>()
        var d = dec
        while(d > 0) {
            bin.add(0, d % 2)
            d /= 2
        }
        return bin.toIntArray()
    }

    private fun decToBin(dec: Int, digits: Int): IntArray {
        val bin = ArrayList<Int>(digits)
        var d = dec
        var c = 0
        while(d > 0) {
            bin.add(0, d % 2)
            d /= 2
            c++
        }
        for(i in 0 until digits-c) {
            bin.add(0, 0)
        }
        return bin.toIntArray()
    }

    private fun binToDec(binary: IntArray): Int {
        var dec = 0
        for((i, b) in binary.withIndex()) {
            dec += b * 2.0.pow((binary.size-i-1).toDouble()).toInt()
        }
        return dec
    }

    private fun binToString(binary: IntArray): String {
        var str = ""
        for (b in binary) {
            str += b
        }
        return str
    }

    fun geohashToCoords(geohash: String): GeoPoint {
        val comp =
            getComponents(geohash)
        return GeoPoint(
            componentToLat(
                comp[0]
            )/* + LAT_PER_GEOHASH / 2*/,
            componentToLon(comp[1])/* + LON_PER_GEOHASH / 2*/)
    }

    fun geohashToCoordsCenter(geohash: String): GeoPoint {
        val comp =
            getComponents(geohash)
        return GeoPoint(
            componentToLat(comp[0]) + LAT_PER_GEOHASH / 2,
            componentToLon(comp[1]) + LON_PER_GEOHASH / 2)
    }

    fun componentToLat(comp: Int): Double {
        return comp.toDouble() / MAX_COMPONENT * 180.0 - 90.0
    }

    fun componentToLon(comp: Int): Double {
        return comp.toDouble() / MAX_COMPONENT * 360.0 - 180.0
    }

    fun geohashesInBounds(bottomLeft: GeoPoint, topRight: GeoPoint): List<String> {
        val startComps = getComponents(
            geohash(
                bottomLeft.latitude,
                bottomLeft.longitude
            )
        )
        val endComps = getComponents(
            geohash(
                topRight.latitude,
                topRight.longitude
            )
        )
        val width = (endComps[0]+1) - (startComps[0]-1)
        val height = (endComps[1]+1) - (startComps[1]-1)
        val geohashes = arrayListOf<String>()
        var i = 0
        while(i < width) {
            var j = 0
            while(j < height) {
                geohashes.add(
                    componentsToGeohash(
                        (startComps[0] - 1) + i,
                        (startComps[1] - 1) + j++
                    )
                )
            }
            i++
        }
        return geohashes
    }

    fun getBounds(geohash: String): List<Double> {
        val comp =
            getComponents(geohash)
        return listOf<Double>(
            componentToLat(
                comp[0]
            ),
            componentToLon(comp[1]),
            componentToLat(comp[0] + 1),
            componentToLon(comp[1] + 1)
        )
    }
}