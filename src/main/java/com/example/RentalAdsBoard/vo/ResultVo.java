package com.example.RentalAdsBoard.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultVo {
    private Integer stateCode;
    private Object obj;
    private String msg;


    public ResultVo success(){return new ResultVo(200,null,"success");}
    public ResultVo success(Object obj){return new ResultVo(200,obj,"success");}
    public ResultVo error(String msg){return new ResultVo(500,null,msg);}
    public ResultVo error(Object obj, String msg){return new ResultVo(500,obj,msg);}

}
