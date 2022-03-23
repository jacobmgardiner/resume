package com.yoloapps.reactiontimetracker.android

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.yoloapps.reactiontimetracker.DateUtils
import com.yoloapps.reactiontimetracker.data.ObservableData
import java.util.*

class DatePickerFragment(val oldDate: Long) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    var date = ObservableData<Long>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        DateUtils.dateFromMs(oldDate).also {
            // Create a new instance of DatePickerDialog and return it
            return DatePickerDialog(requireContext(), this, it[0], it[1], it[2])
        }
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
//        Log.d("XXXXXX", "MONTH PICKED: $month")
        date.data = DateUtils.dateToMs(year, month, day)
    }
}
