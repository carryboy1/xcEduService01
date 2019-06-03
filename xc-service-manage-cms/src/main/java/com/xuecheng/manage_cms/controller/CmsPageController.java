package com.xuecheng.manage_cms.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-09-12 17:24
 **/
@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    PageService pageService;

    @DeleteMapping("/del/{id}")
    public ResponseResult delete(@PathVariable("id") String id){
        return pageService.delete(id);
    }


    @Override
    @PutMapping("/edit/{id}")
    public CmsPageResult updata(@PathVariable("id") String id,@RequestBody CmsPage cmsPage) {
        return pageService.updata(id,cmsPage);
    }

    //根据页面id查询页面是否存在
    @Override
    @GetMapping("/get/{id}")
    public CmsPage findById( @PathVariable("id")  String id) {
        return pageService.findById(id);
    }

    @PostMapping("/add")
    @Override
    //把请求过来的数据转换成json对象
    public CmsPageResult add(@RequestBody  CmsPage cmsPage) {
        return pageService.add(cmsPage);
    }

    @Override
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size")int size, QueryPageRequest queryPageRequest) {
     /*   //暂时用静态数据
        //定义queryResult
        QueryResult<CmsPage> queryResult =new QueryResult<>();
        List<CmsPage> list = new ArrayList<>();
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageName("测试页面");
        list.add(cmsPage);
        queryResult.setList(list);
        queryResult.setTotal(1);

        QueryResponseResult queryResponseResult = new QueryResponseResult(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;*/
        //调用service
        return pageService.findList(page,size,queryPageRequest);
    }
}
