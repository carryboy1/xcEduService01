package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;


import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.aggregation.ArithmeticOperators;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * cms页面的增删该查
 * @author Administrator
 * @version 1.0
 * @create 2018-09-12 18:32
 **/
@Service
public class PageService {

    @Autowired
    CmsPageRepository cmsPageRepository;
    /**
     * 根据id删除页面
     */
    public ResponseResult delete(String id){
        Optional<CmsPage> byId = cmsPageRepository.findById(id);
        if(byId.isPresent()){
            cmsPageRepository.deleteById(id);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);

    }


    /**
     * 根据页面id查询页面
     * @param id
     * @return
     */
        public  CmsPage  findById(String id){
            Optional<CmsPage> id1 = cmsPageRepository.findById(id);
            if(id1.isPresent()){
                CmsPage cmsPage = id1.get();
                return cmsPage;
            }
            return  null;
        }

    /**
     * 修改页面
     * @param id
     * @param cmsPage
     * @return
     */
  public  CmsPageResult updata(String id,CmsPage cmsPage){
     //根据页面id查询页面信息
      CmsPage one = this.findById(id);
      if(one != null){
          //准备更新数据
          //更新所属站点      
          one.setTemplateId(cmsPage.getTemplateId());
           //更新页面别名        
          one.setSiteId(cmsPage.getSiteId());
          one.setPageAliase(cmsPage.getPageAliase());
           //           //更新页面名称        
           one.setPageName(cmsPage.getPageName());
          //       更新访问路径          
          one.setPageWebPath(cmsPage.getPageWebPath());
           //     更新物理路径         
           one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
           //      执行更新         
           CmsPage save = cmsPageRepository.save(one);
          return  new CmsPageResult(CommonCode.SUCCESS,cmsPage);
      }
      return new CmsPageResult(CommonCode.FAIL,null);
  }

    /**
     * 新增页面
     * @return
     */
    public CmsPageResult add(CmsPage cmsPage){
            //校验页面名称、站点id、页面的webpath
            //根据页面名称、站点id、页面的webpath去cms——page集合，如果查到说明页面已经存在，如果查询不到在继续添加
            CmsPage cmsPage1  =  cmsPageRepository.findByPageNameAndSiteIdAndPageWebPath(
                    cmsPage.getPageName(),cmsPage.getSiteId(),cmsPage.getPageWebPath());
            if(cmsPage1 == null){
                //调用dao添加 页面
                cmsPage.setSiteId(null);
                cmsPageRepository.save(cmsPage);
                return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
            }
            //添加失败
        return new CmsPageResult(CommonCode.FAIL,null);
    }


    /**
     * 页面查询方法
     * @param page 页码，从1开始记数
     * @param size 每页记录数
     * @param queryPageRequest 查询条件
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest){
       if(queryPageRequest == null){
           queryPageRequest = new QueryPageRequest();
       }
        //自定义条件查询
        //定义查询条件
        ExampleMatcher exampleMatcher =ExampleMatcher.matching().withMatcher("pageAlisa",
               ExampleMatcher.GenericPropertyMatchers.contains() );
       //条件值对象
        CmsPage cmsPage = new CmsPage();
        //设置条件值
        if(StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        if(StringUtils.isNotEmpty(queryPageRequest.getTemplateId())){
            cmsPage.setSiteId(queryPageRequest.getTemplateId());
        }
        if(StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
            cmsPage.setSiteId(queryPageRequest.getPageAliase());
        }
        //定义条件对象
        Example<CmsPage> example =Example.of(cmsPage,exampleMatcher);
        //分页参数
        if(page <=0){
            page = 1;
        }
        page = page -1;
        if(size<=0){
            size = 10;
        }
        Pageable pageable = PageRequest.of(page,size);
        Page<CmsPage> all = cmsPageRepository.findAll(example,pageable);
        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent());//数据列表
        queryResult.setTotal(all.getTotalElements());//数据总记录数
        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }
}
