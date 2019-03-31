package com.neobuchaemyj.datingapp.Fragments


import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.neobuchaemyj.datingapp.DB.AppDatabase
import com.neobuchaemyj.datingapp.Model.User
import com.neobuchaemyj.datingapp.R
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class SingleOrCoupleFragment : Fragment() {

    private var fragmentMain = androidx.fragment.app.Fragment()
    lateinit var radioGroup: RadioGroup
    lateinit var radioButton: RadioButton
    lateinit var checkedHaveHouse: TextView
    lateinit var checkedHaveCar: TextView
    lateinit var checkedHaveMoto: TextView
    lateinit var makeClear: EditText
    lateinit var nextButton: Button
    lateinit var checkedRadioText: String
    lateinit var db: AppDatabase
    var otherData = ""
    var houseCheked = ""
    var carCheked = ""
    var motoCheked = ""
    var userId = 0
    var user = User()

    @SuppressLint("CheckResult")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_single_or_couple, container, false)

        radioGroup = view.findViewById(R.id.radioGroupLast)
        checkedHaveHouse = view.findViewById(R.id.have_house_text_view)
        checkedHaveCar = view.findViewById(R.id.have_car_text_view)
        checkedHaveMoto = view.findViewById(R.id.have_motorcycle_text_view)
        makeClear = view.findViewById(R.id.make_clear_edit_text)
        nextButton = view.findViewById(R.id.second_next_button)
        db = AppDatabase.getInstance(this.requireContext()) as AppDatabase

        Observable.fromCallable { db.userDao().getLastId() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                userId = it
            }, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })

        nextButton.setOnClickListener {
            radioButton = view.findViewById(radioGroup.checkedRadioButtonId)
            checkedRadioText = radioButton.text.toString()
            otherData = houseCheked + carCheked + motoCheked
            userSet()
            if (userId != 0) {
                saveToDb()
                fragmentMain = LastRegFragment()
                setFragment(fragmentMain)
            } else {
                Toast.makeText(this.requireContext(), "Подождите", Toast.LENGTH_SHORT).show()
            }
        }

            checkedHaveHouse.setOnClickListener {
                houseCheked = if (houseCheked == "") {
                    it.setBackgroundColor(Color.parseColor("#F5F5F5"))
                    checkedHaveHouse.text.toString() + ";"
                } else {
                    it.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    ""
                }
            }
            checkedHaveCar.setOnClickListener {
                carCheked = if (carCheked == "") {
                    checkedHaveCar.setBackgroundColor(Color.parseColor("#F5F5F5"))
                    checkedHaveCar.text.toString() + ";"
                } else {
                    checkedHaveCar.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    ""
                }
            }

            checkedHaveMoto.setOnClickListener {
                motoCheked = if (motoCheked == "") {
                    it.setBackgroundColor(Color.parseColor("#F5F5F5"))
                    checkedHaveMoto.text.toString() + ";"
                } else {
                    it.setBackgroundColor(Color.parseColor("#FFFFFF"))
                    ""
                }
            }


        return view
    }


    @SuppressLint("CheckResult")
    fun userSet() {
        user.setOtherData(otherData)
        user.setStatus(checkedRadioText)
        user.setMakeClear(makeClear.text.toString())
    }

    @SuppressLint("CheckResult")
    fun saveToDb() {
        Completable.fromAction { db.userDao().updateotherData(user.getOtherData(), userId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })
        Completable.fromAction { db.userDao().updateStatus(user.getStatus(), userId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })
        Completable.fromAction { db.userDao().updateMakeClear(user.getMakeClear(), userId) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({}, {
                Log.d("SaveToDb", "Didn't saved in Registration Fragment", it)
            })
    }


    fun setFragment(f: androidx.fragment.app.Fragment) {

        val fm: androidx.fragment.app.FragmentManager = this.requireActivity().supportFragmentManager
        val ft: androidx.fragment.app.FragmentTransaction = fm.beginTransaction()

        ft.replace(R.id.question_container, f)
        ft.commit()

    }



}
