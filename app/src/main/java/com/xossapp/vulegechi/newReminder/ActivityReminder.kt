package com.xossapp.vulegechi.newReminder

import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.xossapp.vulegechi.newReminder.data.ReminderItem
import com.xossapp.vulegechi.R
import com.xossapp.vulegechi.databinding.ActivityReminderBinding
import com.xossapp.vulegechi.home.ActivityHome
import com.xossapp.vulegechi.newReminder.fragment.DatePickerFragment
import com.xossapp.vulegechi.sharedPreference.PreferencesProvider
import com.xossapp.vulegechi.signUp.ActivityOtp
import com.xossapp.vulegechi.utils.Constants
import kotlinx.android.synthetic.main.activity_reminder.*
import java.util.*

class ActivityReminder : AppCompatActivity() {

    private lateinit var activityReminderBinding: ActivityReminderBinding
    private lateinit var viewModel:ActivityReminderViewModel

    private lateinit var preferencesProvider: PreferencesProvider

    private lateinit var remindrDate:String
    private lateinit var reminderTime:String
    private lateinit var reminderId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // binding, viewmodel
        activityReminderBinding = DataBindingUtil.setContentView(this, R.layout.activity_reminder)
        viewModel = ViewModelProvider(this).get(ActivityReminderViewModel::class.java)

        // sharedpreferences
        preferencesProvider = PreferencesProvider(applicationContext)
        reminderId = intent.getStringExtra(Constants.KEY_REMINDER_ID).toString()

        // calender
        val myCalender = Calendar.getInstance()

        activityReminderBinding.apply {
            tvAddCalender.setOnClickListener{
                setDate()
            }

            tvAddTime.setOnClickListener{
                val hourOfDay = myCalender.get(Calendar.HOUR_OF_DAY)
                val minuteOfDay = myCalender.get(Calendar.MINUTE)

                TimePickerDialog(this@ActivityReminder,TimePickerDialog.OnTimeSetListener{view,hour,minute ->
                    activityReminderBinding.tvReminderTime.text = "$hour:$minute"
                    reminderTime = "$hour:$minute"
                },hourOfDay,minuteOfDay,false).show()
            }

            btnReminderSet.setOnClickListener {
                progrssBarReminder.visibility = View.VISIBLE
                setReminder()
            }

            btnReminderDelete.setOnClickListener {
                progrssBarReminder.visibility = View.VISIBLE
                deleteRemind()
            }

            val reminderType = intent.getStringExtra(Constants.KEY_REMINDER_DELETE)
            if(reminderType.equals(Constants.KEY_REMINDER_DELETE))
            {
                setData()
                btnReminderSet.visibility = View.GONE
                btnReminderDelete.visibility = View.VISIBLE
            }

            imvBackReminder.setOnClickListener {
                super.onBackPressed()
            }
        }
    }

    private fun setData()
    {
        val ocassionTittle = intent.getStringExtra(Constants.KEY_OCASSION_TITLE)
        val ocassionPersonType = intent.getStringExtra(Constants.KEY_OCASSION_PERSON_TYPE)
        val ocassionPersonName = intent.getStringExtra(Constants.KEY_OCASSION_PERSON_NAME)
        val ocassionPersonMobile = intent.getStringExtra(Constants.KEY_PERSON_MOBILE)

        activityReminderBinding.apply {
            etOcassionName.text = Editable.Factory.getInstance().newEditable(ocassionTittle)
            etOcassionPerson.text = Editable.Factory.getInstance().newEditable(ocassionPersonName)
            etPersonType.text = Editable.Factory.getInstance().newEditable(ocassionPersonType)
            etPersonMobile.text = Editable.Factory.getInstance().newEditable(ocassionPersonMobile)

            etOcassionName.setEnabled(false)
            etOcassionPerson.setEnabled(false)
            etPersonType.setEnabled(false)
            etPersonMobile.setEnabled(false)
        }
    }

    private fun setDate()
    {
        val datePickerFragment = DatePickerFragment()
        val supportFragmentManager = supportFragmentManager

        supportFragmentManager.setFragmentResultListener(
            "REQUEST_KEY",
            this
        ) { resultKey, bundle ->
            if (resultKey == "REQUEST_KEY") {
                val date = bundle.getString("SELECTED_DATE")
                activityReminderBinding.tvReminderDate.text = date
                remindrDate = date.toString()
            }
        }

        // show
        datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
    }


    private fun setReminder()
    {
        val ocassionName = activityReminderBinding.etOcassionName.text.toString()
        val personType = activityReminderBinding.etPersonType.text.toString()
        val ocassionPersonName = activityReminderBinding.etOcassionPerson.text.toString()
        val personNumber = activityReminderBinding.etPersonMobile.text.toString()
        val mobile = preferencesProvider.getString(Constants.KEY_MOBILE_NUMBER).toString()

        if(ocassionName.isEmpty()||personType.isEmpty()||ocassionPersonName.isEmpty()||
            personNumber.isEmpty()||!::remindrDate.isInitialized||!::reminderTime.isInitialized)
        {
            Toast.makeText(this,"Please Provide The Information",Toast.LENGTH_SHORT).show()
            progrssBarReminder.visibility = View.GONE
        }
        else
        {
            val reminder = ReminderItem(null,mobile, ocassionName,personType,remindrDate,
                ocassionPersonName,personNumber,reminderTime,null)

            viewModel.postNewReminder(reminder)

            viewModel.createReminderListObserver().observe(this, androidx.lifecycle.Observer {
                if(it  == null) {
                    activityReminderBinding.progrssBarReminder.visibility = View.GONE
                    Toast.makeText(this, "Failed to set Reminder", Toast.LENGTH_LONG).show()
                } else {
                    activityReminderBinding.progrssBarReminder.visibility = View.GONE
                    Toast.makeText(this, "Successfully Reminder Set", Toast.LENGTH_LONG).show()
                    nextActivity()
                }
            })
        }
    }

    private fun deleteRemind()
    {
        viewModel.deleteReminder(reminderId)

        viewModel.deleteReminderListObserver().observe(this, androidx.lifecycle.Observer {
            if(it  == null) {
                activityReminderBinding.progrssBarReminder.visibility = View.GONE
                Toast.makeText(this, "Check Network", Toast.LENGTH_LONG).show()
            } else {
                activityReminderBinding.progrssBarReminder.visibility = View.GONE
                Toast.makeText(this, "Delete Reminder Successfully", Toast.LENGTH_LONG).show()
                nextActivity()
            }
        })
    }

    private fun nextActivity()
    {
        val intent = Intent(this, ActivityHome::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent)
    }
}