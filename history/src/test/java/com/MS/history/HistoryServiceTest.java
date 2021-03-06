package com.MS.history;

import com.MS.history.model.*;
import com.MS.history.model.DTO.*;
import com.MS.history.repository.*;
import com.MS.history.service.HistoryService;
import com.MS.history.service.SequenceGeneratorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@SpringBootTest
public class HistoryServiceTest {

    @InjectMocks
    private HistoryService historyService;
    @Mock
    private HistoryRepository historyRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private PurchaseRepository purchaseRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private SequenceGeneratorService sequenceGeneratorService;
    @Mock
    private MongoTemplate mongoTemplate;

    private History history;
    private HistoryDTO historyDTO;
    private HistoryDTO newHistoryDTO;
    private PaymentMethod paymentMethod;
    private PaymentDTO paymentDTO;
    private Products products;
    private ProductsDTO productsDTO;
    private Purchases purchases;
    private Purchases purchasesCheckout;
    private PurchasesDTO purchasesDTO;
    private User user;
    private User newUser;
    private UserDTO userDTO;
    private CheckoutHistory checkoutHistory;
    private UserForm userForm;
    private ModelMapper mapperReal = new ModelMapper();

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
        createAll();
    }

    @Test
    void dateFormatterTest(){
        assertEquals(DATEBR, historyService.dateFormatter(DATELOCAL));
    }

    @Test
    void getByIdTest(){
        when(userRepository.findByUser_id(1l)).thenReturn(user);
        when(historyRepository.findByHistory_id(1l)).thenReturn(history);
        when(userRepository.findByHistory_id(1l)).thenReturn(user);
        newHistoryDTO = mapperReal.map(history,HistoryDTO.class);
        when(modelMapper.map(any(), eq(HistoryDTO.class))).thenReturn(historyDTO);

        assertEquals(newHistoryDTO,historyService.getById(1l));
    }


    void createAll(){
        user = new User(1l,FNAME,LNAME,SEX,CPF,BIRTH,EMAIL,1l);
        userDTO = new UserDTO(1l,FNAME,LNAME,SEX,CPF,BIRTH,EMAIL);
        paymentMethod = new PaymentMethod(1l,TYPE,DSCNT,true,1l);
        products = new Products(1l,PNAME,DESCR,COLOR,SIZE,PRICE,5,1l);
        purchases = new Purchases(1l,paymentMethod, List.of(products),TOTAL,DATEBR,1l);
        history = new History(1l,user,List.of(purchases));
        paymentDTO = new PaymentDTO(TYPE, DSCNT,true);
        productsDTO = new ProductsDTO(PNAME,DESCR,COLOR,SIZE,PRICE,5);
        checkoutHistory = new CheckoutHistory(1l,paymentDTO,List.of(productsDTO),TOTAL,DATELOCAL);
        userForm = new UserForm(FNAME,LNAME,SEX,CPF,BIRTH,EMAIL);
        purchasesDTO = new PurchasesDTO(paymentDTO,List.of(productsDTO),TOTAL,DATEBR);
        historyDTO = new HistoryDTO(userForm,List.of(purchasesDTO));
    }

    static final String DATELOCAL = "2022-05-27";
    static final String DATEBR = "27/05/2022";
    static final String FNAME = "Firstname";
    static final String LNAME = "Lastname";
    static final String SEX = "Masculino";
    static final String CPF = "000.000.000-00";
    static final String BIRTH = "19/04/1997";
    static final String EMAIL = "teste@teste.com";
    static final String TYPE = "Credit";
    static final Double DSCNT = 15.00;
    static final String PNAME = "Product";
    static final String DESCR = "Lorem Ipsum";
    static final String COLOR = "Red";
    static final String SIZE = "G";
    static final Double PRICE = 120.00;
    static final Double TOTAL = 510.00;
}
