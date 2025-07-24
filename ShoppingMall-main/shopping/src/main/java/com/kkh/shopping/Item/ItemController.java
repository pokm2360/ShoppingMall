package com.kkh.shopping.Item;

import com.kkh.shopping.comment.Comment;
import com.kkh.shopping.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor // jpa 연결
public class ItemController { // public 있으면 다른폴더에서도 쓸 수 있음

    private final ItemRepository itemRepository; //db입출력을 위해 함수 생성
    private final ItemService itemService;
//    private final S3Service s3Service;
    private final CommentRepository commentRepository;

//    @Autowired
//    public ItemController(ItemRepository itemRepository) {
//        this.itemRepository = itemRepository;
//    } -- new ItemRepository()하나 뽑아서 변수에 넣으라고 하는 것



    @GetMapping("/list")
    String list(Model model) {
//        itemRepository.findAll(); // 테이블의 모든 데이터 가져옴
//        List<Item> result = itemRepository.findAll();
//        System.out.println(result.get(0)); // item클래스에서 뽑아온 Object 표시
//        model.addAttribute("items", result);
//        var a = result;
//        System.out.println(a.toString());

//        var b = new Item();
//        System.out.println(b.title);

//        List<Object> a = new ArrayList<>(); // Arraylist의 상위호환

//        model.addAttribute("name", "kkh");
        itemService.list(model);
        return "list.html";
    }

    @GetMapping("/write")
    String write() {
        return "write.html";
    }

    @PostMapping("/add")
        // url 작명시 되도록 명사로
//    String addPost(@RequestParam Map<String, String> formData) { // String - 해당 타입으로 변환해라 / Map자료형은 여러 데이터를 한 변수에 넣고싶을때 사용 {key=value} 형태
//    String addPost(@ModelAttribute Item item) { // ModelAttribute: 유저가 보낸 데이터를 object로 쉽게 변환
    String addPost(String title, Integer price) {

//        Item item = new Item();
//        item.setTitle(formData.get("title"));
//        item.setPrice(Integer.parseInt(formData.get("price")));
//        if ((item.getTitle() == null || item.getTitle().trim().isEmpty()) // 제목이 비어있는지 확인
//         && (item.getPrice() == null // 가격이 비어있는지 확인
//                || item.getPrice() <= 0 // 가격이 1원 이상인지 확인
//                || item.getPrice() > 100000000) // 가격이 1억 미만인지 확인
//                || !String.valueOf(item.getPrice()).matches("\\d+(\\.\\d+)?")) { // 가격이 숫자인지 확인
//            return "error.html";
//        } else {
////            itemRepository.save(item); // 데이터를 db에 저장
//        }

//        var test = new HashMap<>();
//        test.put("name", "kim");
//        test.put("age", 20);
//        System.out.println(test.get("name"));
//        new Item()
//        itemRepository.save() // 안에 Item에서 object를 뽑아내 저장
        itemService.saveItem(title, price);
        return "redirect:/list";
    }

    @GetMapping("/detail/{id}")
        // /detail/문자 로 이동 -- url 파라미터 -- 여러 개 연결해서 사용가능
    String detail(@PathVariable Long id, Long parentId, Model model) throws Exception { // url파라미터에 입력한 값 / throw Exception 붙여서 에러처리
//        throw new Exception();
//        try {
//            Optional<Item> result = itemRepository.findById(id); // id가 1인 데이터 가져옴
////            throw new Exception(); -- 에러를 인위적으로 만듬
//            if (result.isPresent()) { // result가 비어있으면 에러가 생기기때문에 있는지 확인해야함!!
//                model.addAttribute("data", result.get());
//                return "detail.html";
//            } else {
//                System.out.println("해당 ID의 상품이 존재하지 않습니다.");
////                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("에러!!") ; // 유저잘못 400 서버 500 정상 200
//                return "error.html";
//            }
//        } catch () {
////            System.out.println(e.getMessage());
//            return "redirect:/list"; // 존재하지 않는 ID라면 목록으로 리다이렉트
//        }
        itemService.detail(id, model);
        commentRepository.findAllByParentId(parentId);
        List<Comment> comments = commentRepository.findAllByParentId(id);
        model.addAttribute("comments", comments);

        itemService.detail(id, model);
        return "detail.html";

    }

    @GetMapping("/edit/{id}")
    String edit(@PathVariable Long id, Model model) {

        try {
            itemService.edit(id, model);
            return "edit.html";
        }  catch (IllegalArgumentException e) {
            return "redirect:/list"; // 예외 발생 시 /list로 이동
        }

    }

    @PostMapping("/edit")
    String editItem(Long id, String title, Integer price) {

        try {
            itemService.editItem(id, title, price);
            return "redirect:/list";
        } catch (IllegalArgumentException e) {
            return "redirect:/list";
        }
    }

    @DeleteMapping("/delete/{abc}") // @Pathvariable 사용하면 됨
    ResponseEntity<String> deleteItem(@RequestParam Long id) {

        try {
            itemService.deleteItem(id);  // 서비스에서 상품 삭제 처리
        } catch (IllegalArgumentException e) {
            // 예외 처리: 삭제할 상품이 없으면 리다이렉트
            return ResponseEntity.status(200).body("삭제완료");
        }
        return ResponseEntity.status(400).body("삭제되지 않았습니다.");
    }

    @GetMapping("/test2") // @Pathvariable 사용하면 됨
    String item() {
        var a = new BCryptPasswordEncoder().encode("문자~~~");
        System.out.println(a);
        return "redirect:/list";

    }

    @GetMapping("/list/page/{page}")
    String getListPage(Model model, @PathVariable Integer page) {

//        Slice<Item> result = itemRepository.findPageBy(PageRequest.of(page-1,5)); // 몇번째 페이지/페이지당 개수
////        result.getTotalPages() 전체 페이지개수 .hasNext 다음페이지가 있는지
        Page<Item> result = itemRepository.findPageBy(PageRequest.of(page - 1, 5)); // 5개씩 페이지네이션
        model.addAttribute("items", result.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", result.getTotalPages());
        return "list.html";
    }

//    @GetMapping("/presigned-url")
//    @ResponseBody
//    String getURL(@RequestParam String filename) {
//
//        var result = s3Service.createPresignedUrl("test/" + filename);
//        System.out.println(result);
//        return result;
//
//    }
// 상품 검색 기능
//    @PostMapping("/search/{searchText}")
//    String postSearch(@RequestParam String searchText, Model model) {
//
////        var result = itemRepository.findAllByTitleContains(searchText);
////        System.out.println(result);
//        var result2 =itemRepository.rawQuery1(searchText);
//        System.out.println(result2);
//        model.addAttribute("items", result2); // 검색결과 모델에 담기
//
//        return "list.html";
//    }
        @GetMapping("/search/{searchText}")
        public String searchItems(@PathVariable String searchText, Model model) {
            var result2 = itemRepository.rawQuery1(searchText);
            model.addAttribute("items", result2);
            return "list.html";
        }
        // 페이지네이션
        @GetMapping("/search/{keyword}/page/{page}")
        public String searchWithPagination(@PathVariable String keyword,
                                           @PathVariable int page,
                                           Model model) {
            Page<Item> result = itemRepository.findPageByTitle(keyword, PageRequest.of(page - 1, 8)); // 한 페이지당 5개
            model.addAttribute("items", result.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", result.getTotalPages());
            model.addAttribute("keyword", keyword);
            return "list.html";
        }

        @GetMapping("/main")
        public String mainPage() {
        return "main.html";
        }



//    @PostMapping("/test") // ajax로 보낸 데이터 출력
//    String test(@RequestBody Map<String, Object> body) { // 특정 object로 바로 변환도 가능 ex) Item
//        System.out.println(body.get("name"));
//        return "redirect:/list";
//    }
//
//    @GetMapping("/test") // 쿼리스트링으로 요청한 데이터 출력
//    String test1(@RequestParam String name) { // 특정 object로 바로 변환도 가능 ex) Item
//        System.out.println(name);
//        return "redirect:/list";
//    }


}

//html에 서버데이터 넣어서 보내주려면
//1. Model model 추가
//2. model.addAttribute(작명, 데이터)
//3. th:text="${작명}"

//1. th:each 반복문을 쓰면 html 반복생성이 가능합니다.
//2. 서버에서 List 자료같은걸 보내면 th:each 반복문으로 List 안에 있던 내용 하나하나 출력도 가능합니다.
//3. object 안의 변수들을 전부 한번에 출력쉽게하고 싶으면 클래스에 @ToString 붙여둡시다.

// REST API는 @ControllerAdvice로 에러처리하는게 편함!!
// 타임리프 사용시엔 error.html로 간단하게 처리

// 쿼리스트링