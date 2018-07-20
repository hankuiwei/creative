package com.cofco.fragment;

import com.cofco.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
public class FragmentSquare extends BaseFragment implements OnClickListener
{	
		private Button mbackButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{

		if (container == null) 
		{
            // Currently in a layout without a container, so no
            // reason to create our view.
            return null;
        }
		
		LayoutInflater myInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
	    View layout = myInflater.inflate(R.layout.frag_square, container, false); 
		
		return layout;
	}

	@Override
	protected void findViewById(View view) {
		mbackButton = (Button) view.findViewById(R.id.back);
	}

	@Override
	protected void processLogic() {
		
	}

	@Override
	protected void setListener() {
		mbackButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back:
			break;

		default:
			break;
		}
	}
	
	
	
}
