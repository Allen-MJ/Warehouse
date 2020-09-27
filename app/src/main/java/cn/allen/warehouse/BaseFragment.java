package cn.allen.warehouse;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

    public void onStartFragment(Fragment fragment){
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment, null)
                .addToBackStack(null)
                .commit();
//        ((MainActivity)getActivity()).startFragmentAdd(fragment);
    }
    public void backPreFragment(){
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
