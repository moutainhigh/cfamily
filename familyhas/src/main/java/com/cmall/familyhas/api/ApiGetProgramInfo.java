package com.cmall.familyhas.api;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cmall.familyhas.api.input.ApiGetProgramInfoInput;
import com.cmall.familyhas.api.model.Program;
import com.cmall.familyhas.api.model.ProgramRelProduct;
import com.cmall.familyhas.api.model.ShareProgram;
import com.cmall.familyhas.api.result.ApiGetProgramInfoResult;
import com.cmall.familyhas.util.DateUtil;
import com.srnpr.zapcom.basemodel.MDataMap;
import com.srnpr.zapdata.dbdo.DbUp;
import com.srnpr.zapweb.webapi.RootApiForManage;

public class ApiGetProgramInfo extends RootApiForManage<ApiGetProgramInfoResult, ApiGetProgramInfoInput>{

	public ApiGetProgramInfoResult Process(ApiGetProgramInfoInput inputParam,
			MDataMap mRequestMap) {
		ApiGetProgramInfoResult result = new ApiGetProgramInfoResult();
		//由于除最新的节目外，如果超过二十条，则页面展示更多按钮，因此暂时查前22条数据
		List<MDataMap> programList = DbUp.upTable("fh_program").query("program_code,title,play_time,status,count,on_time,link,vedio_img", "-play_time,-count", "status='449746740002'", null, 0, 22);
		//最新节目
		String newProg = "";
		if(programList.size() > 0) {
			MDataMap newProgram = programList.get(0);
			Program program = new Program();
			program.setVedio_img(newProgram.get("vedio_img"));
			program.setTitle(newProgram.get("title"));
			String[] tm = newProgram.get("play_time").split("-");
			program.setPlay_time(tm[0]+tm[1]+tm[2]);
			program.setOn_time(newProgram.get("on_time"));
			program.setLink(newProgram.get("link"));
			program.setCount("第"+newProgram.get("count")+"集");
			result.setProgram(program);
			//记录最新节目编号
			newProg = newProgram.get("program_code");
			if(programList.size() > 21) {
				result.setIsMore("1001");
			}
			int programListSize = programList.size();
			if(result.getIsMore().equals("1001")) {
				programListSize = 21;
			}
			//往期精彩
			for (int i = 1; i < programListSize; i++) {
				MDataMap mDataMap = programList.get(i);
				Program pro = new Program();
				pro.setVedio_img(mDataMap.get("vedio_img"));
				pro.setTitle(mDataMap.get("title"));
				String[] tmm = mDataMap.get("play_time").split("-");
				pro.setPlay_time(tmm[0]+tmm[1]+tmm[2]);
				pro.setOn_time(mDataMap.get("on_time"));
				pro.setLink(mDataMap.get("link"));
				pro.setCount("第"+mDataMap.get("count")+"集");
				result.getOldPrograms().add(pro);
			}
			//同款推荐
			String new2ProgramCode = "";
			int count = 0;
			for (int i = 0; i < programList.size(); i++) {
				MDataMap dataMap = programList.get(i);
				String program_code = dataMap.get("program_code");
				if(i >= 2) {
					break;
				} else {
					new2ProgramCode += program_code+",";
					count++;
				}
			}
			String sqlWhere = "";
			String[] newCode = new2ProgramCode.split(",");
			if(count == 2) {
				sqlWhere += " and prog.program_code in ('"+newCode[0]+"','"+newCode[1]+"')";
			} else if(count == 1) {
				sqlWhere += " and prog.program_code in ('"+newCode[0]+"')";
			}
			SimpleDateFormat format = new SimpleDateFormat(DateUtil.DATE_FORMAT_DATEONLY);
			String curntDate = format.format(new Date());
			new2ProgramCode = new2ProgramCode.substring(0, new2ProgramCode.length() - 1);
			String orderStr = " ORDER BY prog.play_time DESC,prog.count DESC,prod.sort DESC";
//			String sqlStr = "SELECT prod.*,pc_prod.mainpic_url,pc_prod_eve.sales FROM familyhas.fh_program_product prod " +
//					"	LEFT JOIN familyhas.fh_program prog ON prog.program_code = prod.program_code and prog.status='449746740002' " +sqlWhere +
//					"	 JOIN productcenter.pc_productinfo pc_prod ON pc_prod.product_code = prod.product_code" +
//					"	LEFT JOIN productcenter.pc_productsales_everyday pc_prod_eve ON pc_prod.product_code = pc_prod_eve.product_code  AND  pc_prod_eve.`day` = '"+curntDate+"'  GROUP BY prod.product_code " ;
			String sqlStr = "SELECT prod.*,pc_prod.mainpic_url FROM familyhas.fh_program_product prod JOIN familyhas.fh_program prog  ON prog.program_code = prod.program_code AND prog.`status` = '449746740002' " +
					" AND prog.uid IS NOT NULL  " +sqlWhere +
					"JOIN productcenter.pc_productinfo pc_prod ON pc_prod.product_code = prod.product_code  GROUP BY prod.product_code";
			
			List<Map<String, Object>> dataSqlList = DbUp.upTable("fh_program_product").dataSqlList(sqlStr + orderStr, null);
			for (Map<String, Object> map : dataSqlList) {
				ProgramRelProduct prod = new ProgramRelProduct();
				prod.setProduct_code(map.get("product_code").toString());
				prod.setProduct_img(map.get("mainpic_url").toString());
				prod.setProduct_name(map.get("product_name").toString());
				if(newProg.equals(map.get("program_code"))) {
					prod.setStatus("1001");
				} else {
					prod.setStatus("1000");
				}
				result.getNewrecommend().add(prod);
			}
			orderStr = "  ORDER BY pc_prod_eve.sales DESC ";
			//所有商品
			sqlStr = "SELECT prod.*,pc_prod.mainpic_url,pc_prod_eve.sales FROM familyhas.fh_program_product prod " +
					"	LEFT JOIN familyhas.fh_program prog ON prog.program_code = prod.program_code and prog.status='449746740002' " +
					"	 JOIN productcenter.pc_productinfo pc_prod ON pc_prod.product_code = prod.product_code" +
					"	LEFT JOIN productcenter.pc_productsales_everyday pc_prod_eve ON pc_prod.product_code = pc_prod_eve.product_code  AND  pc_prod_eve.`day` = '"+curntDate+"'  GROUP BY prod.product_code " ;
			List<Map<String, Object>> allProcucts = DbUp.upTable("fh_program_product").dataSqlList(sqlStr + orderStr, null);
			for (Map<String, Object> map : allProcucts) {
				ProgramRelProduct prod = new ProgramRelProduct();
				prod.setProduct_code(map.get("product_code").toString());
				prod.setProduct_img(map.get("mainpic_url").toString());
				prod.setProduct_name(map.get("product_name").toString());
				if(newProg.equals(map.get("program_code"))) {
					prod.setStatus("1001");
				} else {
					prod.setStatus("1000");
				}
				result.getAllProducts().add(prod);
			}
		} else {
			
		}
		//栏目介绍及分享信息
		List<MDataMap> queryAll = DbUp.upTable("fh_program_wardrobe").queryAll("*", "", "", null);
		for (MDataMap mDataMap : queryAll) {
			
			result.setCategoryDes(mDataMap.get("wardrobe_describe"));
			ShareProgram sharepro = new ShareProgram();
			sharepro.setShare_content(mDataMap.get("share_content"));
			sharepro.setShare_img_url(mDataMap.get("share_img"));
			sharepro.setShare_link(mDataMap.get("share_link"));
			sharepro.setShare_title(mDataMap.get("share_title"));
			result.setShareProgram(sharepro);
		}
		
		return result;
	}
}
