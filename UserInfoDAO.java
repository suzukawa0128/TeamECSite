package com.internousdev.orion.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.internousdev.orion.dto.UserInfoDTO;
import com.internousdev.orion.util.DBConnector;
public class UserInfoDAO {
	public int createUserData(String userId, String password, String familyName, String firstName, String familyNameKana, String firstNameKana, String sex, String email) {
		DBConnector db = new DBConnector();
		Connection conn =db.getConnection();
		int count = 0;
		String sql = "insert into user_info(user_id, password, family_name, first_name, family_name_kana, first_name_kana, sex, email, status, logined, regist_date, update_date)"
				+" values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), now())";
		try{
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1,userId);
			ps.setString(2,password);
			ps.setString(3,familyName);
			ps.setString(4,firstName);
			ps.setString(5,familyNameKana);
			ps.setString(6,firstNameKana);
			ps.setString(7,sex);
			ps.setString(8,email);
			ps.setInt(9, 0);
			ps.setInt(10, 1);
			count = ps.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
		try{
			conn.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return count;
	}
	//ユーザー登録時のユーザーIDチェック(boolean)
	public boolean checkUserIdInfo(String userId){
		DBConnector db=new DBConnector();
		Connection conn = db.getConnection();
		boolean result = false;
		String sql = "select count(*) as count from user_info where user_id = ?";
		try{
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setString(1, userId);
			ResultSet rs=ps.executeQuery();
			while(rs.next()){
				if(rs.getInt("count")>0){
					result = true;
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
		try{
			conn.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		}
		return result;
	}
//ここでは、ユーザーIDとパスワードがデータベースにあるかを調べる処理をしています。
	public boolean isExistUserInfo(String userId, String password) {
		DBConnector db = new DBConnector();
		Connection conn = db.getConnection();
		boolean result = false;
		String sql = "select count(*) as count from user_info where user_id=? and password=?";
		try{
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				if (rs.getInt("count") > 0) {
					result = true;
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			conn.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
//	下のメソッドでは、ユーザーIDがデータベースにあるかを調べる処理をしています。
	public boolean isExistUserInfo(String userId) {
		DBConnector db = new DBConnector();
		Connection conn = db.getConnection();
		boolean result = false;
		String sql = "select count(*) as count from user_info where user_id=?" ;
		try{
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()) {
				if (rs.getInt("count") > 0) {
					result = true;
				}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		try {
			conn.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}
//140行目からのgetUserInfoメソッドでは、ユーザーＩＤとパスワードを引数として使ってデータベースのテーブルに問い合わせて
//情報があった場合、テーブルの情報をUserInfoDTOに値を返しています。
	public UserInfoDTO getUserInfo(String userId, String password){
		DBConnector db = new DBConnector();
		Connection conn = db.getConnection();
		UserInfoDTO userInfoDTO = new UserInfoDTO();
		String sql = "select * from user_info where user_id=? and password=?";
		try{
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				userInfoDTO.setId(rs.getInt("id"));
				userInfoDTO.setUserId(rs.getString("user_id"));
				userInfoDTO.setPassword(rs.getString("password"));
				userInfoDTO.setFamilyName(rs.getString("family_name"));
				userInfoDTO.setFirstName(rs.getString("first_name"));
				userInfoDTO.setFamilyNameKana(rs.getString("family_name_kana"));
				userInfoDTO.setFirstNameKana(rs.getString("first_name_kana"));
				userInfoDTO.setSex(rs.getInt("sex"));
				userInfoDTO.setEmail(rs.getString("email"));
				userInfoDTO.setUserId(rs.getString("status"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try{
			conn.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return userInfoDTO;
	}
	//157行目からのgetUserInfoメソッドでは、ユーザーＩＤを引数として使ってデータベースのテーブルに問い合わせて
	//情報があった場合、テーブルの情報をUserInfoDTOに値を返しています。
	public UserInfoDTO getUserInfo(String userId){
		DBConnector db = new DBConnector();
		Connection conn = db.getConnection();
		UserInfoDTO userInfoDTO = new UserInfoDTO();
		String sql = "select * from user_info where user_id=?" ;
		try{
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, userId);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				userInfoDTO.setId(rs.getInt("id"));
				userInfoDTO.setUserId(rs.getString("user_id"));
				userInfoDTO.setPassword(rs.getString("password"));
				userInfoDTO.setFamilyName(rs.getString("family_name"));
				userInfoDTO.setFirstName(rs.getString("first_name"));
				userInfoDTO.setFamilyNameKana(rs.getString("family_name_kana"));
				userInfoDTO.setFirstNameKana(rs.getString("first_name_kana"));
				userInfoDTO.setSex(rs.getInt("sex"));
				userInfoDTO.setEmail(rs.getString("email"));
				userInfoDTO.setUserId(rs.getString("logined"));
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		try{
			conn.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return userInfoDTO;
	}
//189行目からは、パスワードの設定を新しく上書きするための処理文を書いています。
	public int resetPassword(String userId, String password) {
		DBConnector db = new DBConnector();
		Connection conn = db.getConnection();
		String sql = "update user_info set password=?, update_date=now() where user_id=?";
		int result = 0;
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, password);
			ps.setString(2, userId);
			result = ps.executeUpdate();
		}catch (SQLException e){
			e.printStackTrace();
		}
		try{
			conn.close();
		}	catch(SQLException e) {
				e.printStackTrace();
		}
		return result;
	}
//ここでは、ログインしているユーザーのID情報を調べる処理をしています。
	public boolean loginCheck(String loginUserId, String loginPassword){
		String sql = "SELECT user_id, password FROM user_info WHERE user_id = ? AND password = ?";
		boolean ret = false;
		Connection conn = null;
		try{
			DBConnector db = new DBConnector();
			conn = db.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, loginUserId);
			ps.setString(2, loginPassword);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				ret = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				conn.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		return ret;
	}
}