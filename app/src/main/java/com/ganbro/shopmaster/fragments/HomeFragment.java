package com.ganbro.shopmaster.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.ganbro.shopmaster.R;
import com.ganbro.shopmaster.activities.ProductDetailActivity;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find all product cards
        LinearLayout productCard1Left = view.findViewById(R.id.product_card_1_left);
        LinearLayout productCard1Right = view.findViewById(R.id.product_card_1_right);
        LinearLayout productCard2Left = view.findViewById(R.id.product_card_2_left);
        LinearLayout productCard2Right = view.findViewById(R.id.product_card_2_right);
        LinearLayout productCard3Left = view.findViewById(R.id.product_card_3_left);
        LinearLayout productCard3Right = view.findViewById(R.id.product_card_3_right);

        // Set click listeners
        setCardClickListener(productCard1Left);
        setCardClickListener(productCard1Right);
        setCardClickListener(productCard2Left);
        setCardClickListener(productCard2Right);
        setCardClickListener(productCard3Left);
        setCardClickListener(productCard3Right);

        return view;
    }

    private void setCardClickListener(LinearLayout card) {
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                startActivity(intent);
            }
        });
    }
}
