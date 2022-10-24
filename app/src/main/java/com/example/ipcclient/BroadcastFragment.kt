package com.example.ipcclient

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.os.Process.myPid
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ipcclient.RecentClient.DATA
import com.example.ipcclient.RecentClient.PACKAGE_NAME
import com.example.ipcclient.RecentClient.PID
import com.example.ipcclient.databinding.FragmentBroadcastBinding
import java.util.*

class BroadcastFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentBroadcastBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBroadcastBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.btnConnect.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        sendBroadcast()
        showBroadcastTime()
    }

    private fun sendBroadcast(){
        val intent = Intent()
        intent.action = "com.example.ipcclient"
        intent.putExtra(PACKAGE_NAME, context?.packageName)
        intent.putExtra(PID, myPid().toString())
        intent.putExtra(DATA, binding.edtClientData.text.toString())
        /*FLAG_INCLUDE_STOPPED_PACKAGES flag is added to the intent before it is sent to indicate
         that the intent is to be allowed to start a component of a stopped application.
         This flag insures that the broadcast reaches out even apps not running*/
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
        intent.component = ComponentName("com.example.ipcserver", "com.example.ipcserver.IPCBroadcastReceiver")
        activity?.applicationContext?.sendBroadcast(intent)
    }

    private fun showBroadcastTime(){
        val cal = Calendar.getInstance()
        val time ="${cal.get(Calendar.HOUR)}:${cal.get(Calendar.MINUTE)}:${cal.get(Calendar.SECOND)}"
        binding.linearLayoutClientInfo.visibility = View.VISIBLE
        binding.txtDate.text = time
    }
}