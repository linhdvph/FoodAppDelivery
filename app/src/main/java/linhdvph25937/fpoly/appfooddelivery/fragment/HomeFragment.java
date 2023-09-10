package linhdvph25937.fpoly.appfooddelivery.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import linhdvph25937.fpoly.appfooddelivery.R;
import linhdvph25937.fpoly.appfooddelivery.adapter.FoodAdapter;
import linhdvph25937.fpoly.appfooddelivery.adapter.FoodTypeAdapter;
import linhdvph25937.fpoly.appfooddelivery.adapter.FragmentProductHomeAdapter;
import linhdvph25937.fpoly.appfooddelivery.adapter.SpinnerTypeProductAdapter;
import linhdvph25937.fpoly.appfooddelivery.model.Food;
import linhdvph25937.fpoly.appfooddelivery.model.FoodType;
import linhdvph25937.fpoly.appfooddelivery.model.ImageUrl;
import linhdvph25937.fpoly.appfooddelivery.model.Receiver;
import linhdvph25937.fpoly.appfooddelivery.ultil.MyRetrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    private ImageSlider imageSlider;
    private RecyclerView rvHighRate, rvProductAdmin;
    private ArrayList<Food> listFood;
    private FoodAdapter foodAdapter;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ArrayList<FoodType> listFoodType;
    public static final String TAG = newInstance().getTag();
    private SharedPreferences sharedPreferences;
    private TextView tvAddress, tvUsername, tvSearch;
    private ScrollView scrollViewUser;
    private Button btnShowDialogAddProduct;
    private FrameLayout layout_admin;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Initial(view);
        setValueInitial();
        AutoSlideImage();//Tạo ảnh chạy tự động
        setTabLayoutAndViewPager2();
        getListProductType();//Lấy danh sách loại sản phẩm
        showDialogAddProduct();
        actionSearch();//Thực hiện tìm kiếm
        return view;
    }

    private void actionSearch() {
        tvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_DarkActionBar);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_dialog_search);
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                ImageView imgBack = dialog.findViewById(R.id.img_arrow_back_dialog_search_in_home_fragment);
                EditText edSearch = dialog.findViewById(R.id.edSearchDialog);
                RecyclerView rvProduct = dialog.findViewById(R.id.rv_all_product_for_search);
                RecyclerView rvTypeProduct = dialog.findViewById(R.id.rv_type_product);

                imgBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                FoodTypeAdapter foodTypeAdapter = new FoodTypeAdapter(listFoodType);
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2, RecyclerView.VERTICAL, false);
                rvTypeProduct.setLayoutManager(gridLayoutManager);
                rvTypeProduct.setAdapter(foodTypeAdapter);

                dialog.show();
            }
        });
    }

    private void showDialogAddProduct() {
        btnShowDialogAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_DarkActionBar);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_dilalog_add_product_by_admin);
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                EditText edNameProduct = dialog.findViewById(R.id.ed_name_product_add);
                EditText edPrice = dialog.findViewById(R.id.ed_price_product_add);
                EditText edAddressProduct = dialog.findViewById(R.id.ed_address_product_add);
                EditText edImgProduct = dialog.findViewById(R.id.ed_image_product_add);
                EditText edTime = dialog.findViewById(R.id.ed_time_product_add);
                EditText edEnergy = dialog.findViewById(R.id.ed_energy_product_add);
                EditText edRating = dialog.findViewById(R.id.ed_rating_product_add);
                EditText edDescriptions = dialog.findViewById(R.id.ed_descriptions_product_add);
                Button btnAdd = dialog.findViewById(R.id.btn_add_product_by_admin);
                ImageView imgBack = dialog.findViewById(R.id.img_arrow_back_add_product_by_admin);

                Spinner spinner = dialog.findViewById(R.id.spinner_type_product_in_add_product);
                SpinnerTypeProductAdapter spinnerTypeProductAdapter = new SpinnerTypeProductAdapter(getContext(), listFoodType);
                spinner.setAdapter(spinnerTypeProductAdapter);

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = edNameProduct.getText().toString();
                        String price = edPrice.getText().toString();
                        String address = edAddressProduct.getText().toString();
                        String img = edImgProduct.getText().toString();
                        String time = edTime.getText().toString();
                        String energy = edEnergy.getText().toString();
                        String rating = edRating.getText().toString();
                        String descriptions = edDescriptions.getText().toString();
                        String typeProduct = ((FoodType) spinner.getSelectedItem()).getId();
                        int priceParse = 0, timeParse = 0;
                        double ratingParse = 0, energyParse = 0;
                        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(price) || TextUtils.isEmpty(address) || TextUtils.isEmpty(img)
                                || TextUtils.isEmpty(time) || TextUtils.isEmpty(energy) || TextUtils.isEmpty(rating) || TextUtils.isEmpty(descriptions)) {
                            Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            priceParse = Integer.parseInt(price);
                        } catch (NumberFormatException e) {
                            Toast.makeText(getContext(), "Giá sản phẩm phải là số", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            timeParse = Integer.parseInt(time);
                        } catch (NumberFormatException e) {
                            Toast.makeText(getContext(), "Thời gian phải là số", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            ratingParse = Double.parseDouble(rating);
                        } catch (NumberFormatException e) {
                            Toast.makeText(getContext(), "Đánh giá phải là số", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            energyParse = Double.parseDouble(energy);
                        } catch (NumberFormatException e) {
                            Toast.makeText(getContext(), "Calo phải là số", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (ratingParse > 5.0 || ratingParse < 0.1) {
                            Toast.makeText(getContext(), "Đánh giá nằm trong khoảng từ 0.0 - 5.0", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        MyRetrofit.retrofit.addProduct(new Food(name, typeProduct, priceParse, address, img, timeParse, energyParse, ratingParse, descriptions))
                                .enqueue(new Callback<Food>() {
                                    @Override
                                    public void onResponse(Call<Food> call, Response<Food> response) {
                                        if (response.body() != null) {
                                            Toast.makeText(getContext(), "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                            listFood.add(response.body());
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(getContext(), "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Food> call, Throwable t) {
                                        Log.e(TAG, "Lỗi server thêm sản phẩm: " + t);
                                        Toast.makeText(getActivity(), "Lỗi server thêm sản phẩm", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                    }
                });

                imgBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });
    }

    private void setTabLayoutAndViewPager2() {
        FragmentProductHomeAdapter fragmentAdapter = new FragmentProductHomeAdapter(getActivity());
        viewPager2.setAdapter(fragmentAdapter);
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Tất cả");
                        break;
                    case 1:
                        tab.setText("Đánh giá");
                        break;
                }
            }
        });
        mediator.attach();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void AutoSlideImage() {
        ArrayList<SlideModel> imageList = new ArrayList<>();
        MyRetrofit.retrofit.getLinkImageBanner().enqueue(new Callback<Receiver>() {
            @Override
            public void onResponse(Call<Receiver> call, Response<Receiver> response) {
                if (response.body() != null) {
                    ArrayList<ImageUrl> listUrl = response.body().getListImageUrl();
                    for (int i = 0; i < listUrl.size(); i++) {
                        imageList.add(new SlideModel(listUrl.get(i).getUrl(), ScaleTypes.FIT));
                    }
                    imageSlider.setImageList(imageList);
                } else {
                    Log.e(TAG, "onResponse: Không thể lấy danh sách loại sản phẩm");
                }
            }

            @Override
            public void onFailure(Call<Receiver> call, Throwable t) {
                Log.e(TAG, "Lỗi server khi lấy danh sách url image: " + t);
                Toast.makeText(getActivity(), "Lỗi server khi lấy danh sách url image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setValueInitial() {
        String phoneNumber = sharedPreferences.getString("phone_number_sign_in", "");
        MyRetrofit.retrofit.getUserByPhoneNumber(phoneNumber)
                .enqueue(new Callback<Receiver>() {
                    @Override
                    public void onResponse(Call<Receiver> call, Response<Receiver> response) {
                        if (response.body() != null) {
                            String username = response.body().getData().getName();
                            String address = sharedPreferences.getString("address", "");
                            if (sharedPreferences.getBoolean("isSignIn", false) == true) {
                                tvAddress.setText(address);
                                tvUsername.setText(username);
                            } else {
                                tvAddress.setText("Address");
                                tvUsername.setText("Username");
                            }
                            if (response.body().getData().getRole() == 0) {
                                scrollViewUser.setVisibility(View.GONE);
                                layout_admin.setVisibility(View.VISIBLE);
                                getAllProductForAdmin();
                            } else {
                                scrollViewUser.setVisibility(View.VISIBLE);
                                layout_admin.setVisibility(View.GONE);
                                getNewListProduct();//Lấy danh sách sản phẩm mới nhất
//                                setTabLayoutAndViewPager2();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Receiver> call, Throwable t) {
                        Log.e(TAG, "Lỗi server khi lấy thông tin người dùng: " + t);
                        Toast.makeText(getActivity(), "Lỗi server khi lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getAllProductForAdmin() {
        MyRetrofit.retrofit.getAllProduct().enqueue(new Callback<Receiver>() {
            @Override
            public void onResponse(Call<Receiver> call, Response<Receiver> response) {
                if (response.body() != null) {
                    ArrayList<Food> listAllFood = response.body().getListFood();
                    foodAdapter = new FoodAdapter(listAllFood, 3);
                    crudWithAdmin(listAllFood);
                    LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    rvProductAdmin.setLayoutManager(manager);
                    rvProductAdmin.setAdapter(foodAdapter);
                }
            }

            @Override
            public void onFailure(Call<Receiver> call, Throwable t) {

            }
        });
    }

    private void crudWithAdmin(ArrayList<Food> list) {
        foodAdapter.setItemOnClick(new FoodAdapter.ItemOnClick() {
            @Override
            public void UpdateProduct(int position, Food objFood) {
                Dialog dialog = new Dialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_DarkActionBar);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_dilalog_update_product_by_admin);
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

                EditText edNameProduct = dialog.findViewById(R.id.ed_name_product_update);
                edNameProduct.setText(objFood.getName());
                EditText edPrice = dialog.findViewById(R.id.ed_price_product_update);
                edPrice.setText(objFood.getPrice() + "");
                EditText edAddressProduct = dialog.findViewById(R.id.ed_address_product_update);
                edAddressProduct.setText(objFood.getAddress());
                EditText edImgProduct = dialog.findViewById(R.id.ed_image_product_update);
                edImgProduct.setText(objFood.getAvatar());
                EditText edTime = dialog.findViewById(R.id.ed_time_product_update);
                edTime.setText(objFood.getTime() + "");
                EditText edEnergy = dialog.findViewById(R.id.ed_energy_product_update);
                edEnergy.setText(objFood.getEnergy() + "");
                EditText edRating = dialog.findViewById(R.id.ed_rating_product_update);
                edRating.setText(objFood.getRating() + "");
                EditText edDescriptions = dialog.findViewById(R.id.ed_descriptions_product_update);
                edDescriptions.setText(objFood.getDescriptions());
                Button btnUpdate = dialog.findViewById(R.id.btn_update_product_by_admin);
                ImageView imgBack = dialog.findViewById(R.id.img_arrow_back_update_product_by_admin);

                Spinner spinner = dialog.findViewById(R.id.spinner_type_product_in_update_product);
                SpinnerTypeProductAdapter spinnerTypeProductAdapter = new SpinnerTypeProductAdapter(getContext(), listFoodType);
                spinner.setAdapter(spinnerTypeProductAdapter);

                for (int i = 0; i < listFoodType.size(); i++) {
                    if (objFood.getIdTypeProduct().getId().equals(listFoodType.get(i).getId())) {
                        spinner.setSelection(i);
                        break;
                    }
                }

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = edNameProduct.getText().toString();
                        String price = edPrice.getText().toString();
                        String address = edAddressProduct.getText().toString();
                        String img = edImgProduct.getText().toString();
                        String time = edTime.getText().toString();
                        String energy = edEnergy.getText().toString();
                        String rating = edRating.getText().toString();
                        String descriptions = edDescriptions.getText().toString();
                        String typeProduct = ((FoodType) spinner.getSelectedItem()).getId();
                        int priceParse = 0, timeParse = 0;
                        double ratingParse = 0, energyParse = 0;
                        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(price) || TextUtils.isEmpty(address) || TextUtils.isEmpty(img)
                                || TextUtils.isEmpty(time) || TextUtils.isEmpty(energy) || TextUtils.isEmpty(rating) || TextUtils.isEmpty(descriptions)) {
                            Toast.makeText(v.getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            priceParse = Integer.parseInt(price);
                        } catch (NumberFormatException e) {
                            Toast.makeText(v.getContext(), "Giá sản phẩm phải là số", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            timeParse = Integer.parseInt(time);
                        } catch (NumberFormatException e) {
                            Toast.makeText(v.getContext(), "Thời gian phải là số", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            ratingParse = Double.parseDouble(rating);
                        } catch (NumberFormatException e) {
                            Toast.makeText(v.getContext(), "Đánh giá phải là số", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            energyParse = Double.parseDouble(energy);
                        } catch (NumberFormatException e) {
                            Toast.makeText(v.getContext(), "Calo phải là số", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (ratingParse > 5.0 || ratingParse < 0.1) {
                            Toast.makeText(v.getContext(), "Đánh giá nằm trong khoảng từ 0.0 - 5.0", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        int finalPriceParse = priceParse;
                        int finalTimeParse = timeParse;
                        double finalEnergyParse = energyParse;
                        double finalRatingParse = ratingParse;
                        MyRetrofit.retrofit.updateProduct(objFood.getId(), new Food(objFood.getId(), name, typeProduct, priceParse, descriptions, img, address, timeParse, energyParse, ratingParse))
                                .enqueue(new Callback<Food>() {
                                    @Override
                                    public void onResponse(Call<Food> call, Response<Food> response) {
                                        if (response.isSuccessful()) {
                                            Toast.makeText(v.getContext(), "Sửa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                            list.set(position, new Food(objFood.getId(), name, typeProduct, finalPriceParse, descriptions, img, address, finalTimeParse, finalEnergyParse, finalRatingParse));
                                            foodAdapter.notifyDataSetChanged();
                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(v.getContext(), "Sửa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Food> call, Throwable t) {
                                        Log.e("ErrorUpdateFood", "onFailure: " + t);
                                        Toast.makeText(v.getContext(), "Lỗi server khi sửa sản phẩm", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                    }
                });

                imgBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }

            @Override
            public void DeleteProduct(int position, Food objFood) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Xóa sản phẩm")
                        .setIcon(android.R.drawable.ic_delete)
                        .setMessage("Bạn có chắc muốn xóa sản phẩm này?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MyRetrofit.retrofit.deleteProduct(objFood.getId())
                                        .enqueue(new Callback<Food>() {
                                            @Override
                                            public void onResponse(Call<Food> call, Response<Food> response) {
                                                if (response.isSuccessful()) {
                                                    Toast.makeText(getContext(), "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                                    list.remove(position);
                                                    foodAdapter.notifyDataSetChanged();
                                                    dialog.dismiss();
                                                } else {
                                                    Toast.makeText(getContext(), "Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Food> call, Throwable t) {
                                                Log.e("ErrorDeleteFood", "onFailure: " + t);
                                                Toast.makeText(getContext(), "Lỗi server khi xóa sản phẩm", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    private void getListProductType() {
        MyRetrofit.retrofit.getAllProductType().enqueue(new Callback<Receiver>() {
            @Override
            public void onResponse(Call<Receiver> call, Response<Receiver> response) {
                if (response.body() != null) {
                    listFoodType = response.body().getListFoodType();
                } else {
                    Log.e(TAG, "onResponse: Không thể lấy danh sách loại sản phẩm");
                }
            }

            @Override
            public void onFailure(Call<Receiver> call, Throwable t) {
                Log.e(TAG, "Lỗi server khi lấy danh sách loại sản phẩm: " + t);
                Toast.makeText(getActivity(), "Lỗi server khi lấy danh sách loại sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getNewListProduct() {
        MyRetrofit.retrofit.getProductHighetRating().enqueue(new Callback<Receiver>() {
            @Override
            public void onResponse(Call<Receiver> call, Response<Receiver> response) {
                if (response.body() != null) {
                    listFood = response.body().getListFood();
                    foodAdapter = new FoodAdapter(listFood, 2);
                    rvHighRate.setAdapter(foodAdapter);
                    Log.e(TAG, "onResponse: " + listFood.get(0).getName());
                } else {
                    Log.e(TAG, "onResponse: Không thể lấy danh sách sản phẩm mới nhất");
                }
            }

            @Override
            public void onFailure(Call<Receiver> call, Throwable t) {
                Log.e(TAG, "Lỗi server khi lấy danh sách sản phẩm mới nhất: " + t);
                Toast.makeText(getActivity(), "Lỗi server khi lấy danh sách sản phẩm mới nhất", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Kiểm tra định vị



    private void Initial(View view) {
        tvUsername = view.findViewById(R.id.tvUsernameHome);
        imageSlider = view.findViewById(R.id.image_slider);
        rvHighRate = view.findViewById(R.id.rvHighRate);
        rvProductAdmin = view.findViewById(R.id.rv_all_product_admin);
        tabLayout = view.findViewById(R.id.tab_layout_home_fragment);
        viewPager2 = view.findViewById(R.id.view_pager2_home_fragment);
        tvAddress = view.findViewById(R.id.tvAddressHome);
        scrollViewUser = view.findViewById(R.id.scrollview_user);
        btnShowDialogAddProduct = view.findViewById(R.id.btn_show_dialog_add_product);
        layout_admin = view.findViewById(R.id.layout_admin_home_fragment);
        tvSearch = view.findViewById(R.id.tv_search);

        listFoodType = new ArrayList<>();
        listFood = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvHighRate.setLayoutManager(manager);
        sharedPreferences = getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
    }
}