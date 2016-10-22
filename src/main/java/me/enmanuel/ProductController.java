package me.enmanuel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by IntelliJ IDEA.
 * User: Enmanuel
 * Date: 27/07/2016
 * Time: 05:26 PM
 */
@Controller
public class ProductController {

    @InitBinder
    public void dataBinding(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(LocalDate.class, new CustomDateEditor(dateFormat, true) {
            @Override
            public String getAsText() {
                return (getValue() != null ? ((LocalDate) getValue()).format(DateTimeFormatter.ISO_DATE) : "");
            }
        });
    }

    @Autowired
    ProductService productService;

    @RequestMapping("/products")
    public ModelAndView products(ModelAndView modelAndView) {
        modelAndView.addObject("products", productService.getProducts());
        modelAndView.setViewName("products");
        return modelAndView;
    }

    @RequestMapping("/")
    public String index() {
        return "redirect:/products";
    }

    @RequestMapping(value = "/product", method = RequestMethod.POST)
    public ModelAndView save(ModelAndView modelAndView, Product product, RedirectAttributes redirectAttributes) {
        productService.save(product);
        redirectAttributes.addFlashAttribute("success", "El producto fue guardado correctamente");
        modelAndView.setViewName("redirect:/products");

        return modelAndView;
    }

    @RequestMapping(value = "/product/{productId}")
    public ModelAndView edit(ModelAndView modelAndView, @PathVariable Integer productId) {

        modelAndView.setViewName("product");
        modelAndView.addObject("product", productService.getProduct(productId));
        return modelAndView;
    }

    @RequestMapping(value = "/product/create")
    public ModelAndView create(ModelAndView modelAndView) {

        modelAndView.setViewName("product");
        modelAndView.addObject("product", new Product());
        return modelAndView;
    }

    @RequestMapping(value = "/product/delete/{productId}")
    public ModelAndView delete(ModelAndView modelAndView, @PathVariable Integer productId, RedirectAttributes redirectAttributes) {
        productService.delete(productService.getProduct(productId));
        redirectAttributes.addFlashAttribute("success", "El producto fue eliminado correctamente");
        modelAndView.setViewName("redirect:/products");
        return modelAndView;
    }

}
