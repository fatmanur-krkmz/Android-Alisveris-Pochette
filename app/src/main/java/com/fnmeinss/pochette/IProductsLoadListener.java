package com.fnmeinss.pochette;

import java.util.List;

public interface IProductsLoadListener {
    void onProductLoadSuccess(List<Product> productModeList);
    void onProductLoadFailed(String message);
}

