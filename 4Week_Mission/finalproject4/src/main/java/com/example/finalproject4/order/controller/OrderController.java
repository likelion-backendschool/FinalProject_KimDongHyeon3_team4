package com.example.finalproject4.order.controller;

import com.example.finalproject4.cart.entity.CartItem;
import com.example.finalproject4.cart.service.CartService;
import com.example.finalproject4.member.entity.Member;
import com.example.finalproject4.member.service.MemberService;
import com.example.finalproject4.order.entity.Order;
import com.example.finalproject4.order.service.OrderService;
import com.example.finalproject4.product.entity.Product;
import com.example.finalproject4.product.service.ProductService;
import com.example.finalproject4.security.dto.SecurityMember;
import com.example.finalproject4.util.Util;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final ProductService productService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper;
    private final OrderService orderService;
    private final MemberService memberService;
    private final CartService cartService;

    @GetMapping("/charge")
    public String orderCharge(@AuthenticationPrincipal SecurityMember securityMember,
                              Model model){

        Member member = securityMember.getMember();

        long restCash = memberService.getRestCash(member);

        Order order = orderService.createByRestCash(member);

        model.addAttribute("order", order);
        model.addAttribute("restCash", restCash);

        return "/order/charge";
    }

    @GetMapping("/create/{productId}")  //개별 결제
    public String createOrder(@AuthenticationPrincipal SecurityMember securityMember,
                              @PathVariable long productId,
                              Model model){

        Product product = productService.findById(productId);
        Member member = securityMember.getMember();

        long restCash = memberService.getRestCash(member);

        Order order = orderService.createByProduct(member, product);

        model.addAttribute("order", order);
        model.addAttribute("restCash", restCash);

        return "/order/detail";
    }

    @GetMapping("/create")  //장바구니 결제
    public String createOrder(@AuthenticationPrincipal SecurityMember securityMember,
                              Model model){

        Member member = securityMember.getMember();

        long restCash = memberService.getRestCash(member);

        List<CartItem> cartItems = cartService.findByBuyer(member);

        if(cartItems.size() == 0){
            return "redirect:/cart/list?errorMsg=" + Util.url.encode("장바구니에 담긴 상품이 없습니다.");
        }

        Order order = orderService.createByCart(member);

        model.addAttribute("order", order);
        model.addAttribute("restCash", restCash);

        return "/order/detail";
    }

    @PostMapping("/{id}/payByRestCash")
    public String payByRestCashOnly(@AuthenticationPrincipal SecurityMember securityMember,
                                    @PathVariable long id) {
        Order order = orderService.findById(id);

        Member member = securityMember.getMember();

        long restCash = memberService.getRestCash(member);

        if (orderService.memberCanPayment(member, order) == false) {
            throw new RuntimeException("현재 회원이 결제를 할 수 없습니다.");
        }

        orderService.payByRestCash(order);

        return "redirect:/product/list?msg=%s".formatted(Util.url.encode("예치금으로 결제했습니다."));
    }

    @GetMapping("/{orderId}/cancle")  //취소
    public String cancelOrder(@AuthenticationPrincipal SecurityMember securityMember,
                              @PathVariable long orderId){

        Order order = orderService.findById(orderId);

        orderService.cancelOrder(order);

        return "redirect:/product/list?msg=" + Util.url.encode("%d의 주문이 취소되었습니다.".formatted(order.getId()));
    }

    @GetMapping("/{orderId}/refund")  //환불
    public String refundOrder(@AuthenticationPrincipal SecurityMember securityMember,
                              @PathVariable long orderId){

        Order order = orderService.findById(orderId);

        if(!orderService.canRefund(order)){
            return "redirect:/member/mybook?errorMsg=" + Util.url.encode("%d의 주문은 환불기간이 종료되어 환불할 수 없습니다.".formatted(order.getId()));
        }


        orderService.refund(order);

        return "redirect:/member/mybook?msg=" + Util.url.encode("%d의 주문이 환불되었습니다.".formatted(order.getId()));
    }

    @PostConstruct
    private void init() {
        restTemplate.setErrorHandler(new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) {
                return false;
            }

            @Override
            public void handleError(ClientHttpResponse response) {
            }
        });
    }

    private final String SECRET_KEY = "test_sk_jZ61JOxRQVEgMjQY2bR8W0X9bAqw";

    @RequestMapping("/{id}/success")
    public String confirmPayment(@AuthenticationPrincipal SecurityMember securityMember,
                                 @PathVariable long id,
                                 @RequestParam String paymentKey,
                                 @RequestParam String orderId,
                                 @RequestParam Long amount,
                                 Model model) throws Exception {

        Order order = orderService.findById(id);
        Member member = securityMember.getMember();

        long orderIdInputed = Long.parseLong(orderId.split("__")[1]);

        if ( id != orderIdInputed ) {
            throw new RuntimeException("생성된 주문이 없습니다.");
        }

        if (orderService.memberCanPayment(member, order) == false) {
            throw new RuntimeException("현재 회원이 결제를 할 수 없습니다.");
        }

        HttpHeaders headers = new HttpHeaders();
        // headers.setBasicAuth(SECRET_KEY, ""); // spring framework 5.2 이상 버전에서 지원
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("orderId", orderId);
        payloadMap.put("amount", String.valueOf(amount));


        long restCash = memberService.getRestCash(member);
        long payPriceRestCash = order.getPayPrice() - amount;

        if (payPriceRestCash > restCash) {
            throw new RuntimeException("예치금이 부족합니다.");
        }

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payloadMap), headers);

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {

            orderService.payByTossPayments(order, payPriceRestCash);

            JsonNode successNode = responseEntity.getBody();
            model.addAttribute("orderId", successNode.get("orderId").asText());
            String secret = successNode.get("secret").asText(); // 가상계좌의 경우 입금 callback 검증을 위해서 secret을 저장하기를 권장함
            return "order/success";
        } else {
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());
            return "/order/fail";
        }
    }

    @RequestMapping("/{id}/fail")
    public String failPayment(@RequestParam String message, @RequestParam String code, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("code", code);
        return "/order/fail";
    }

}
