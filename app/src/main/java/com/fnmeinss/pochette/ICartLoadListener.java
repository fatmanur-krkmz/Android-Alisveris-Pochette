package com.fnmeinss.pochette;

import java.util.List;


public interface ICartLoadListener {
    void onCartLoadSuccess(List<CartModel> CartModeList);
    void onCartLoadFailed(String message);
}
