package com.model2.mvc.service.product.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductDao;
import com.model2.mvc.service.product.ProductService;
@Service("productServiceImpl")
public class ProductServiceImpl implements ProductService {
		@Autowired
		@Qualifier("productDaoImpl")
	private ProductDao productDao;
	
	public void setProdcutDao(ProductDao prodcutDao) {
		this.productDao = prodcutDao;
	}
	
	
	public ProductServiceImpl() {
		// TODO Auto-generated constructor stub
		
		System.out.println(this.getClass());
	}

	@Override
	public void addProduct(Product product) throws Exception {
		// TODO Auto-generated method stub
		productDao.addProduct(product);
	}

	@Override
	public Product getProduct(int prodNo) throws Exception {
		// TODO Auto-generated method stub
		return productDao.getProduct(prodNo);
	}

	@Override
	public Map<String, Object> getProductList(Search search) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("���� ��ġȮ�� "  + search);
		List<Product> list = productDao.getProductList(search);
		int totalCount = productDao.getTotalCount(search);
		System.out.println("����������������������������������");
		System.out.println("����������������������������������");
		System.out.println(totalCount);
		System.out.println(list.toString());
		System.out.println("����������������������������������");
		System.out.println("����������������������������������");
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("totalCount", new Integer(totalCount));
		
		
		
		return map;
	}

	@Override
	public void updateProduct(Product product) throws Exception {
		// TODO Auto-generated method stub
		productDao.updateProduct(product);
	}

}