package com.example.ipcclient

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.ipcclient.databinding.FragmentAidlBinding
import com.example.ipcserver.IIPCExample


class AidlFragment : Fragment(), ServiceConnection, View.OnClickListener {

    private var _binding: FragmentAidlBinding? = null
    private val binding get() = _binding!!
    var iRemoteService: IIPCExample? = null
    private var connected = false
    private lateinit var activity: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = requireActivity() as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAidlBinding.inflate(inflater, container, false)
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
        connected = if (connected) {
            disconnectToRemoteService()
            binding.txtServerPid.text = ""
            binding.txtServerConnectionCount.text = ""
            binding.btnConnect.text = getString(R.string.connect)
            binding.linearLayoutClientInfo.visibility = View.INVISIBLE
            false
        } else {
            connectToRemoteService()
            binding.linearLayoutClientInfo.visibility = View.VISIBLE
            binding.btnConnect.text = getString(R.string.disconnect)
            true
        }
    }

    private lateinit var intent: Intent

    private fun connectToRemoteService() {
        intent = Intent("aidlexample")
        val pack = IIPCExample::class.java.`package`
        pack?.let {
            intent.setPackage(pack.name)
            activity.bindService(
                intent, this, Context.BIND_AUTO_CREATE
            )
        }
    }

    private fun disconnectToRemoteService() {
        if(connected){
            //activity.stopService(intent)
            activity.unbindService(this)
        }
    }

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        // Gets an instance of the AIDL interface named IIPCExample,
        // which we can use to call on the service
        iRemoteService = IIPCExample.Stub.asInterface(service)
        binding.txtServerPid.text = iRemoteService?.pid.toString()
        binding.txtServerConnectionCount.text = iRemoteService?.connectionCount.toString()
        iRemoteService?.setDisplayedValue(
            context?.packageName, Process.myPid(),
            binding.edtClientData.text.toString())
        var ret = iRemoteService?.isValid(context?.packageName)
        Log.e("TAGG", "Client Package name $ret")

        connected = true
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        Toast.makeText(context, "IPC server has disconnected unexpectedly", Toast.LENGTH_LONG).show()
        iRemoteService = null
        connected = false
    }
}