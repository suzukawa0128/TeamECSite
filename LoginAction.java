package com.internousdev.orion.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.internousdev.orion.dao.CartInfoDAO;
import com.internousdev.orion.dao.UserInfoDAO;
import com.internousdev.orion.dto.CartInfoDTO;
import com.internousdev.orion.util.InputChecker;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport implements SessionAware{

	private String loginUserId;
	private String loginPassword;
	private boolean idKeeper; //ユーザーID保持チェック
	private List<String> userIdErrorList = new ArrayList<String>(); //ユーザーIDのInputCheckerの戻り値を格納
	private List<String> passwordErrorList = new ArrayList<String>(); //パスワードのInputCheckerの戻り値を格納
	private String accountCheckErrorMsg; //入力値とDBの照合時に入力値と一致するデータが見つからない場合のエラーメッセージ
	private List<CartInfoDTO> cartInfoDTOList;
	private int totalPrice;
	private Map<String, Object> session;

	public String execute(){
		String result = ERROR;
		boolean ret = false;
		boolean cartInfoRes = false;
//		ユーザーID保持チェックがチェックされている場合は
//		ユーザーIDとユーザーID保持チェックをセッションに格納
		if(idKeeper){
			session.put("savedUserId", loginUserId);
			session.put("idKeeper", idKeeper);
		}else{
			session.remove("savedUserId");
			session.remove("idKeeper");
		}
//		エラーメッセージをリストに入れる
		InputChecker inputChecker = new InputChecker();
		userIdErrorList = inputChecker.doCheck("ユーザーID", loginUserId, 1, 8, true, false, false, true, false, false, false);
		passwordErrorList = inputChecker.doCheck("パスワード", loginPassword, 1, 16, true, false, false, true, false, false, false);
		if(userIdErrorList.size() > 0 || passwordErrorList.size() > 0){
			return result;
		}
//		未入力、桁数、文字種のチェック後に、会員情報テーブルと入力値の照合
		UserInfoDAO userInfoDAO = new UserInfoDAO();
		ret = userInfoDAO.loginCheck(loginUserId, loginPassword);
		if(ret){
//			カートのフラグが立っている場合の処理はここから
//			未ログインの状態でAddCartActionを通るとDBに仮ユーザーIDのカート情報が入る。（その後ヘッダーのログインからログイン）
//			仮ユーザーIDのカート情報があったかどうかDBを元に判別してあったらchangeCartInfoメソッドでDBを書き換える
			CartInfoDAO cartInfoDAO = new CartInfoDAO();
			List<CartInfoDTO> cartInfoDTOList = cartInfoDAO.getUserCartInfo(String.valueOf(session.get("tempId")));
			if(cartInfoDTOList != null){
				cartInfoRes = changeCartInfo(cartInfoDTOList);
				if(!cartInfoRes){
					return "DBError";
				}
			}
			session.put("loginUserId", loginUserId);
			session.put("loggedIn", 1);
//			遷移先の決定
			if(session.containsKey("cartFlg")){
				session.remove("cartFlg");
				result = "cart";
				return result;
			}
			result = SUCCESS;
		}else{
			accountCheckErrorMsg = "ユーザーIDまたはパスワードが異なります。";
		}
		return result;
	}

	private boolean changeCartInfo(List<CartInfoDTO> cartInfoDTOListBySession){
		CartInfoDAO cartInfoDAO = new CartInfoDAO();
		String tempUserId = (String)session.get("tempId");
		boolean ret = false;
		int count = 0;
		for(CartInfoDTO dto : cartInfoDTOListBySession){
			if(cartInfoDAO.existCartInfo(loginUserId, dto.getProductId())){
//				updateCartCountの戻り値は何列更新したか
				count += cartInfoDAO.updateCartCount(loginUserId, dto.getProductId(), dto.getProductCount());
				cartInfoDAO.deleteCartInfo(tempUserId, String.valueOf(dto.getProductId()));
			}else{
				count += cartInfoDAO.tempUserIdUpdate(loginUserId, tempUserId, dto.getProductId());
			}
		}
		if(count == cartInfoDTOListBySession.size()){
			cartInfoDTOList = cartInfoDAO.getUserCartInfo(loginUserId);
			totalPrice = cartInfoDAO.getTotalPrice(loginUserId);
			ret = true;
		}
		return ret;
	}

	public String getLoginUserId() {
		return loginUserId;
	}
	public void setLoginUserId(String loginUserId) {
		this.loginUserId = loginUserId;
	}
	public String getLoginPassword() {
		return loginPassword;
	}
	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	public boolean isIdKeeper() {
		return idKeeper;
	}
	public void setIdKeeper(boolean idKeeper) {
		this.idKeeper = idKeeper;
	}
	public List<String> getUserIdErrorList() {
		return userIdErrorList;
	}
	public void setUserIdErrorList(List<String> userIdErrorList) {
		this.userIdErrorList = userIdErrorList;
	}
	public List<String> getPasswordErrorList() {
		return passwordErrorList;
	}
	public void setPasswordErrorList(List<String> passwordErrorList) {
		this.passwordErrorList = passwordErrorList;
	}
	public String getAccountCheckErrorMsg() {
		return accountCheckErrorMsg;
	}
	public void setAccountCheckErrorMsg(String accountCheckErrorMsg) {
		this.accountCheckErrorMsg = accountCheckErrorMsg;
	}
	public List<CartInfoDTO> getCartInfoDTOList() {
		return cartInfoDTOList;
	}
	public void setCartInfoDTOList(List<CartInfoDTO> cartInfoDTOList) {
		this.cartInfoDTOList = cartInfoDTOList;
	}
	public int getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(int totalPrice) {
		this.totalPrice = totalPrice;
	}
	public Map<String, Object> getSession() {
		return session;
	}
	@Override
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
