package pas.com.mm.shoopingcart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import pas.com.mm.shoopingcart.database.DBListenerCallback;
import pas.com.mm.shoopingcart.database.DbSupport;
import pas.com.mm.shoopingcart.database.model.Item;
import pas.com.mm.shoopingcart.database.model.Model;
import pas.com.mm.shoopingcart.image.FavouritiesImageAdapter;
import pas.com.mm.shoopingcart.image.MobileImageAdapter;
import pas.com.mm.shoopingcart.image.PromotionImageGridAdapter;
import pas.com.mm.shoopingcart.util.ImageCache;
import pas.com.mm.shoopingcart.util.ImageFetcher;
import pas.com.mm.shoopingcart.util.PreferenceUtil;
import pas.com.mm.shoopingcart.util.Sorter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImageGridFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImageGridFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImageGridFragment extends Fragment implements DBListenerCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int mImageThumbSize;
    private int mImageThumbSpacing;
    private static final String IMAGE_CACHE_DIR = "thumbs";
    private OnFragmentInteractionListener mListener;
    private ImageFetcher mImageFetcher;
    private GridView gridview;
    private DbSupport dbSupport;
    private DbSupport dao=new DbSupport();
    private boolean dataLoaded=false;
    private List<Item> list;
    SharedPreferences pf=null;
    private static int panel=-1;
    public ImageGridFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ImageGridFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImageGridFragment newInstance(String param1, String param2) {
        ImageGridFragment fragment = new ImageGridFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mImageThumbSize = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size);
        mImageThumbSpacing = getResources().getDimensionPixelSize(R.dimen.image_thumbnail_spacing);

        pf=getActivity().getPreferences(Context.MODE_PRIVATE);
        ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams(getActivity(), IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory
        Log.i("ImagGrid","actifity aniitialized--------------------");
        mImageFetcher = new ImageFetcher(getActivity(), mImageThumbSize);
       //mImageFetcher.setLoadingImage(R.drawable.ie_loader);
        mImageFetcher.addImageCache(getActivity().getSupportFragmentManager(), cacheParams);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v= inflater.inflate(R.layout.image_grid_fragment, container, false);
        final Context context=this.getContext();
        DbSupport dbsupport=new DbSupport();
         gridview = (GridView) v.findViewById(R.id.gridview_cache);
        MobileImageAdapter  imageAdapter=new MobileImageAdapter(getActivity(),mImageFetcher);
        Bundle b= this.getArguments();
        panel=b.getInt("PANEL");
        if(panel==0)
        {
           // imageAdapter=new FavouritiesImageAdapter(getActivity(),mImageFetcher);
           // gridview.setAdapter(imageAdapter);
            dao.getItemsByType("regular",this);
        }
         if(panel==1)
        {
            dao.getItemsByType("new",this);
            /**
                dbsupport.getItemsByType("EAT",this);

                while(dataLoaded!=true){
                    try {
                        Log.d(this.getClass().getName(),"WAITING DATA");
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
               **/
          //  imageAdapter=new PromotionImageGridAdapter(getActivity(),mImageFetcher,list);

        }
        else if(panel ==2)
        {

            dao.getItemsByType("promo",this);



        }


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
             //   Toast.makeText(getActivity(), "" + position,
               //         Toast.LENGTH_SHORT).show();
               Intent intent = new Intent(getActivity(), DetailActivity.class);
               Item item= list.get(position);
                Gson gson=new Gson();
               String objStr= gson.toJson(item);
                intent.putExtra("DETAIL_ITEM",objStr);
                //intent.putExtra("POSITION", id);

                PreferenceUtil.saveLastAccessItem(pf,item.getType(),item.getCode());
                startActivity(intent);
            //  getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.wrapper, new DetailFragment()).commit();

            }
        });

        final SwipeRefreshLayout mySwipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.swiperefresh);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i("LOGTAG", "onRefresh called from SwipeRefreshLayout");

                        if(panel==2)
                        {
                            dao.getItemsByType("regular",null);
                        }
                        if(panel==1)
                        {
                            dao.getItemsByType("new",null);
                        }
                        else if(panel ==0)
                        {
                            dao.getItemsByType("promo",null);
                        }

                        gridview.invalidateViews();
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        mySwipeRefreshLayout.setRefreshing (false);
                    }
                }
        );
        return v;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void LoadCompleted(boolean b) {
        dataLoaded=true;
        list= dao.getResultList();
        if(list.size()>0) {
            String code = PreferenceUtil.getLastAccessItem(pf, list.get(0).getType());
            list=Sorter.setRecentFirst(list,code);
            PromotionImageGridAdapter imageAdapter = new PromotionImageGridAdapter(getActivity(), mImageFetcher, list);
            gridview.setAdapter(imageAdapter);
        }
    }

    @Override
    public void receiveResult(Model model) {

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
