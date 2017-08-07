package com.model2.mvc.web.purchase;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.user.UserService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.sun.tracing.dtrace.ModuleAttributes;

//==> 회원관리 Controller
@Controller
@RequestMapping("/purchase/*")
public class PurchaseController {

	/// Field
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	// setter Method 구현 않음
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	public PurchaseController() {
		System.out.println(this.getClass());
	}

	// ==> classpath:config/common.properties ,
	// classpath:config/commonservice.xml 참조 할것
	// ==> 아래의 두개를 주석을 풀어 의미를 확인 할것
	@Value("#{commonProperties['pageUnit']}")
	// @Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;

	@Value("#{commonProperties['pageSize']}")
	// @Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;
	
	@RequestMapping("/addPurchaseView")
	public String addPurchaseView(@ModelAttribute("product") Product product, Model model) throws Exception{
		System.out.println("/addPurchaseView");
		
		product = productService.getProduct(product.getProdNo());
		System.out.println(product);
		model.addAttribute("product", product);
		
		return "forward:/purchase/addPurchaseView.jsp";
	}

	@RequestMapping("/addPurchase")
	public String addPurchase(@ModelAttribute("purchase") Purchase purchase, @RequestParam("prodNo") int prodNo, @RequestParam("buyerId") String userId) throws Exception {
		
		
		System.out.println("/addPurchase");
		User user = userService.getUser(userId);
		Product product = productService.getProduct(prodNo);
		System.out.println(purchase);
		purchase.setBuyer(user);
		purchase.setPurchaseProd(product);
		purchaseService.addPurchase(purchase);
		
		

		return "forward:/purchase/addPurchase.jsp";
	}
	
	@RequestMapping("/getPurchase")
	public String getPurchase (@RequestParam("tranNo")int tranNo, Model model) throws Exception{
		
		System.out.println("/getPurchase");
		
		System.out.println(tranNo +"<====tranNo");

		Purchase purchase =purchaseService.getPurchase(tranNo);
		purchase.setDivyDate(purchase.getDivyDate().split(" ")[0]);
		//purchase.setDivyDate(purchase.getDivyAddr().substring(0, 8));
		System.out.println(purchase.getOrderDate()+"ddddddd");
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/getPurchase.jsp";
	}
	
	
	@RequestMapping("/listPurchase")
	public String listPurchaes(@ModelAttribute("search") Search search, 
			@ModelAttribute("user") User user, 
			Model model , HttpSession session) throws Exception {

		System.out.println("/listPurchaese");
		
		//String buyerId = user.getUserId();
		String buyerId = ((User)session.getAttribute("user")).getUserId();
		
		
		System.out.println(buyerId);
		if (search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		
		
		search.setPageSize(pageSize);
		System.out.println("서치" + search);
		System.out.println("커런트페이지"+search.getCurrentPage());
		System.out.println("");

		// Business logic 수행
		Map<String, Object> map = purchaseService.getPurchaseList(search, buyerId);
		System.out.println("여기까지?");
		Page resultPage = new Page(search.getCurrentPage(), ((Integer) map.get("totalCount")).intValue(), pageUnit,
				pageSize);
		System.out.println(resultPage);

		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);


		return "forward:/purchase/listPurchase.jsp";
	}
	
	@RequestMapping("/updatePurchaseView")
	public String updatePurchaseView(@ModelAttribute("purchase") Purchase purchase, Model model) throws Exception{
		
		System.out.println("/updatePurchaseView");
				
		purchase = purchaseService.getPurchase(purchase.getTranNo());
		
	
		System.out.println(purchase +":::::::: purchase");
		
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/updatePurchase.jsp";
	}
	
	
	@RequestMapping("/updatePurchase")
	public String updatePurchase(@ModelAttribute("purchase") Purchase purchase ,@RequestParam("prodNo") int prodNo, 	@ModelAttribute("user") User user, 
			Model model , HttpSession session) throws Exception{
		
		System.out.println("/updatePurchase");
		String buyerId = ((User)session.getAttribute("user")).getUserId();
		System.out.println("prodNo =====>> " + prodNo);
		System.out.println(buyerId);
		System.out.println();
		purchase.setBuyer(userService.getUser(buyerId));
		purchase.setPurchaseProd(productService.getProduct(prodNo));
		//purchase.setPurchaseProd(productService.getProduct(prodNo));
		System.out.println(purchase +"purchase");
		
		purchaseService.updatePurcahse(purchase);
		
		
		
		
		
		return "redirect:/purchase/getPurchase?tranNo="+purchase.getTranNo();
	}
	
	@RequestMapping("/updateTranCodeByProd")
	public String updateTranCodeByProd(@ModelAttribute("purchase") Purchase purchase, @ModelAttribute("product") Product product, @RequestParam("prodNo") int prodNo, @RequestParam("tranCode") String tranCode, @RequestParam("menu") String menu, Model model, HttpServletRequest request)throws Exception{
		System.out.println("updateTranCodeByProd start");
		
		System.out.println(prodNo +"++" +tranCode + "|||" +menu );
		product.setProdNo(prodNo);
//		product.setProTranCode(tranCode);
		purchase.setTranCode(tranCode);
		purchase.setPurchaseProd(product);
		
		purchaseService.updateTranCode(purchase);
//		purchase.setTranCode(tranCode);
//		purchase.setPurchaseProd(product);
		
		//purchaseService.updateTranCode(purchase);
		
		
		System.out.println("어디까지");
		if(menu.equals("manage")){
			return "forward:/product/listProduct?menu=manage";
		}else{
			return "forward:/purchase/listPurchase";
		}
 
	}
	

}