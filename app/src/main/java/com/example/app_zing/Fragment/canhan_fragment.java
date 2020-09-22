package com.example.app_zing.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.app_zing.Adapter.listnhacAdapter;
import com.example.app_zing.R;
import com.example.app_zing.activity.manhinhgiaodien;
import com.example.app_zing.modul.listnhac;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link canhan_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class canhan_fragment extends Fragment {
    FloatingActionButton btnthemspform;
    ImageView anhnhac;
    ListView lvnhaccanhan;
    ArrayList<listnhac> arraylistnhac;
    listnhacAdapter adapter;
    final int REQUREQUES_CODE_FOLDER = 1;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public canhan_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment canhan_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static canhan_fragment newInstance(String param1, String param2) {
        canhan_fragment fragment = new canhan_fragment();
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

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_canhan_fragment, container, false);
        btnthemspform = view.findViewById(R.id.floatingthemnhac);
        lvnhaccanhan = view.findViewById(R.id.lvnhaccanhan);
        arraylistnhac = new ArrayList<>();
//        adapter = new listnhacAdapter(this,R.layout.dong_bai_hat,arraylistnhac);
        lvnhaccanhan.setAdapter(adapter);

        hienthidanhsachnhac();
        lvnhaccanhan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), ""+ i, Toast.LENGTH_SHORT).show();
            }
        });



        btnthemspform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialogformthem();
            }
        });
        return view;





    }

    private void hienthidanhsachnhac() {
        Cursor cursor =  manhinhgiaodien.MyDB.getData("SELECT * FROM nhaccanhan");
        while (cursor.moveToNext()){
            arraylistnhac.add(new listnhac(
                    cursor.getInt(0),
                    cursor.getBlob(1),
                    cursor.getString(2),
                    cursor.getString(3)
            ));
        }
        adapter.notifyDataSetChanged();
    }

    public void Dialogformthem(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them);
        dialog.show();
        anhnhac = dialog.findViewById(R.id.anhnhac);
        final TextInputLayout tenbathat = dialog.findViewById(R.id.tenbathat);
        final TextInputLayout tencasi = dialog.findViewById(R.id.tencasi);
        Button dialogbtnthem = dialog.findViewById(R.id.dialogbtnthem);

        dialogbtnthem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //chuyển hình từ bitmap qua byte[]
                BitmapDrawable bitmapDrawable = (BitmapDrawable) anhnhac.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                //định dạng lại kiểu dữ liệu
                //100 là chất lượng hình ảnh
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
                //chứa dữ liệu
                //chuyển mảng byte
                byte[] hinh = byteArrayOutputStream.toByteArray();

                String tenbaihat = tenbathat.getEditText().getText().toString().trim();

                if (tenbaihat.equals("")){
                    Toast.makeText(getActivity(), "tên bài hát và tên ca sĩ không được trống", Toast.LENGTH_SHORT).show();
                    tenbathat.setError("hông được trống");
                }else {
                    manhinhgiaodien.MyDB.INSERT_NHAC(
                            hinh,
                            tenbathat.getEditText().getText().toString().trim(),
                            tencasi.getEditText().getText().toString().trim()
                    );
                    Toast.makeText(getActivity(), "thanh cong", Toast.LENGTH_SHORT).show();
                    arraylistnhac.clear();
                    hienthidanhsachnhac();
                    dialog.dismiss();
                }
            }
        });
        anhnhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1,REQUREQUES_CODE_FOLDER);
            }
        });


    }
    public void xoabainhac(final String tenbainhacmuonxoa, final int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("bạn có đồng ý xóa sản phẩm '" + tenbainhacmuonxoa + "' không ?");
        builder.setPositiveButton("XÓA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                manhinhgiaodien.MyDB.Querydata("DELETE FROM nhaccanhan WHERE Id = '" + id + "'");
                hienthidanhsachnhac();
            }
        });
        builder.setNegativeButton("hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUREQUES_CODE_FOLDER && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream =  getActivity().getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                anhnhac.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
