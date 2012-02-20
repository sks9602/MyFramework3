package com.base.system.action;

import org.springframework.web.servlet.ModelAndView;

import com.base.system.delegate.DataBaseDelegate;

public class DataBaseAction extends ProjectAction {
	public Object execute(String actionFlag, ModelAndView mav) throws Exception {


		if(actionFlag.equals("r_list")) {
			this.setForwardPage( (String) this.getHoConfig().getOutlineMap().get("MAIN_GRID") );

			DataBaseDelegate delegate = (DataBaseDelegate) super.getHoDelegate();

			delegate.init();

		}
		else if(actionFlag.equals("r_json_data")){
			this.setForwardPage( (String) this.getHoConfig().getOutlineMap().get("DATA_JSON") );

			DataBaseDelegate delegate = (DataBaseDelegate) super.getHoDelegate();

			delegate.list();
		}
		else if(actionFlag.equals("r_detail")){
			super.setForwardPage((String) this.getHoConfig().getOutlineMap().get("MAIN"));

			DataBaseDelegate delegate = (DataBaseDelegate) super.getHoDelegate();

			delegate.detail();
		}
		else if(actionFlag.equals("r_query")){
			super.setForwardPage((String) this.getHoConfig().getOutlineMap().get("MAIN"));

			DataBaseDelegate delegate = (DataBaseDelegate) super.getHoDelegate();

			delegate.detail();
		}
		else if(actionFlag.indexOf("r_html")!=-1){
			super.setForwardPage((String) this.getHoConfig().getOutlineMap().get("MAIN"));

			DataBaseDelegate delegate = (DataBaseDelegate) super.getHoDelegate();

			delegate.detail();
		}	
		else if(actionFlag.equals("r_join_table")){
			super.setForwardPage((String) this.getHoConfig().getOutlineMap().get("MAIN"));

			DataBaseDelegate delegate = (DataBaseDelegate) super.getHoDelegate();

			delegate.joinTable();
		}	
		else if(actionFlag.equals("r_join")){
			super.setForwardPage((String) this.getHoConfig().getOutlineMap().get("MAIN"));

			DataBaseDelegate delegate = (DataBaseDelegate) super.getHoDelegate();

			delegate.join();
		}	
		return null;
	}
}