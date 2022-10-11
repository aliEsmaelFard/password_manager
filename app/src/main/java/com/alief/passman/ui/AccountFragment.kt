package com.alief.passman.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alief.passman.R
import com.alief.passman.adaptors.AccountsAdaptor
import com.alief.passman.adaptors.OnItemAccountClicked
import com.alief.passman.database.MyDao
import com.alief.passman.database.MyDataBase
import com.alief.passman.models.AccountModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AccountFragment : Fragment(), OnItemAccountClicked, OnBottomSheetCloseListener {

    lateinit var recyclerView: RecyclerView
    lateinit var list: MutableList<AccountModel>
    lateinit var adaptor: AccountsAdaptor
    lateinit var dao: MyDao
    private var mContext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_account, container, false)

        dao = MyDataBase.getInstance(mContext!!).getDao()!!
        list = dao.getAccounts()

        adaptor = AccountsAdaptor(list, this, mContext!!)

        recyclerView = view.findViewById(R.id.account_recycler)
        recyclerView.adapter = adaptor
        recyclerView.layoutManager = LinearLayoutManager(mContext)
        recyclerView.setHasFixedSize(true)

        //open button sheet
        val floatActionButton: FloatingActionButton = view.findViewById(R.id.account_fad)
        floatActionButton.setOnClickListener{
            val bottomSheet = AddAccountBottomSheet(this)
            bottomSheet.show(parentFragmentManager, "sheet")
        }

        return view
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }

    override fun onDeleteImageAccount(position: Int)
    {
        GlobalScope.launch(Dispatchers.IO) {
            val account = dao.getAccountById(list[position].id)

            dao.deleteAccount(account)
            list.removeAt(position)
            adaptor.notifyItemRemoved(position)

        }

    }

    override fun onUpdateImageAccount(position: Int)
    {
        val bottomSheet = AddAccountBottomSheet(this, list[position].id)
        bottomSheet.show(parentFragmentManager, "sheet")
    }

    override fun onDismiss()
    {
        val ft: FragmentTransaction = parentFragmentManager.beginTransaction()
        ft.replace(R.id.frame_layout, AccountFragment())
        ft.commit()
    }


}