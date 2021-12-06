package org.rgranger.cacheflow.invoice

import com.fasterxml.jackson.databind.ObjectMapper
import org.rgranger.cacheflow.invoice.domain.Invoice
import org.rgranger.cacheflow.invoice.domain.InvoiceStatus
import org.rgranger.cacheflow.invoice.domain.LineItem
import org.rgranger.cacheflow.invoice.exception.InvalidInvoiceException
import org.rgranger.cacheflow.invoice.exception.InvalidRequestException
import org.rgranger.cacheflow.invoice.resource.InvoiceResource
import org.rgranger.cacheflow.invoice.resource.LineItemResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification
import spock.mock.DetachedMockFactory

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDate
import java.util.stream.Collectors

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration
class InvoiceCtrlTest extends Specification {
    @Autowired
    WebApplicationContext webApplicationContext

    @Autowired
    InvoiceService mockInvoiceService

    @Autowired
    ObjectMapper objectMapper

    MockMvc mockMvc

    static final String INVOICE_URL = "http://localhost/invoices"
    static final Long INVOICE_ID = 1001L
    static final Long LINE_ITEM_ID = 1L
    static final Long INVALID_INVOICE_ID = 0L

    void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .defaultRequest(get(INVOICE_URL).contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE))
                .build()
    }

    @TestConfiguration
    static class MockConfig {
        DetachedMockFactory detachedMockFactory = new DetachedMockFactory()

        @Bean
        InvoiceService invoiceService() {
            detachedMockFactory.Mock(InvoiceService)
        }
    }

    def "Create a new invoice"() {
        given: "a new invoice request"
        InvoiceResource invoiceResource = createInvoiceResource()

        when: "a new invoice create request is made"
        def request = post(INVOICE_URL).content(objectMapper.writeValueAsString(invoiceResource))
        ResultActions response = mockMvc.perform(request)

        then: "a response is returned with a status of CREATED (201)"
        response.andExpect(status().isCreated())

        and: "it has the new invoice id in the location header"
        response.andExpect(header().string("Location", "$INVOICE_URL/$INVOICE_ID"))

        and: "the invoice service is called"
        1 * mockInvoiceService.create(*_) >> { arguments ->
            InvoiceResource invoiceResourceCreateRequest = arguments?.get(0)
            assert invoiceResourceCreateRequest.customerEmail == createInvoiceResource().customerEmail
            assert invoiceResourceCreateRequest.customerName == createInvoiceResource().customerName
            assert invoiceResourceCreateRequest.description == createInvoiceResource().description
            assert invoiceResourceCreateRequest.customerEmail == createInvoiceResource().customerEmail

            INVOICE_ID
        }
    }

    def "Create an invalid invoice"() {
        given: "a new invoice request"
        InvoiceResource invoiceResource = createInvalidInvoiceResource()

        when: "a new invoice create request is made"
        def request = post(INVOICE_URL).content(objectMapper.writeValueAsString(invoiceResource))
        ResultActions response = mockMvc.perform(request)

        then: "a response is returned with a status of UNPROCESSABLE ENTITY (422)"
        response.andExpect(status().isUnprocessableEntity())

        and: "the invoice service is called"
        1 * mockInvoiceService.create(*_) >> {
            throw new InvalidRequestException("Due date is required.")
        }

        and: "the response body contains the error message"
        response.andExpect(content().string("Due date is required."))
    }

    def "Update an existing invoice"() {
        given: "an existing invoice request"
        Long invoiceId = INVOICE_ID
        InvoiceResource invoiceResource = createInvoiceResource()

        when: "a new invoice create request is made"
        def request = put(INVOICE_URL + "/" + invoiceId)
                .content(objectMapper.writeValueAsString(invoiceResource))
        ResultActions response = mockMvc.perform(request)

        then: "a response is returned with a status of OK (200)"
        response.andExpect(status().isOk())

        and: "the invoice service is called"
        1 * mockInvoiceService.update(*_) >> { arguments ->
            Long updateInvoiceId = arguments?.get(0)
            assert updateInvoiceId == invoiceId
            InvoiceResource invoiceResourceUpdateRequest = arguments?.get(1)
            assert invoiceResourceUpdateRequest.customerEmail == createInvoiceResource().customerEmail
            assert invoiceResourceUpdateRequest.customerName == createInvoiceResource().customerName
            assert invoiceResourceUpdateRequest.description == createInvoiceResource().description
            assert invoiceResourceUpdateRequest.customerEmail == createInvoiceResource().customerEmail

            INVOICE_ID
        }
    }

    def "Update an invalid invoice"() {
        given: "an existing invoice request"
        Long invoiceId = INVALID_INVOICE_ID
        InvoiceResource invoiceResource = createInvoiceResource()

        when: "a new invoice create request is made"
        def request = put(INVOICE_URL + "/" + invoiceId)
                .content(objectMapper.writeValueAsString(invoiceResource))
        ResultActions response = mockMvc.perform(request)

        then: "a response is returned with a status of NOT FOUND (404)"
        response.andExpect(status().isNotFound())

        and: "the invoice service is called"
        1 * mockInvoiceService.update(*_) >> { arguments ->
            throw new InvalidInvoiceException(invoiceId)
        }

        and: "the response body contains the error message"
        response.andExpect(content().string("The invoice id " + invoiceId + " does not exist"))
    }

    def "Create a new line item"() {
        given: "a new line item request"
        Long invoiceIdCreateRequest = INVOICE_ID
        LineItemResource lineItemResourceCreateRequest = createLineItemResource()

        when: "a new line item create request is made"
        def request = post("$INVOICE_URL/$invoiceIdCreateRequest/line-items")
                .content(objectMapper.writeValueAsString(lineItemResourceCreateRequest))
        ResultActions response = mockMvc.perform(request)

        then: "a response is returned with a status of CREATED (201)"
        response.andExpect(status().isCreated())

        and: "it has the new invoice id in the location header"
        response.andExpect(header().string("Location", "$INVOICE_URL/$invoiceIdCreateRequest/line-items/$LINE_ITEM_ID"))

        and: "the invoice service is called"
        1 * mockInvoiceService.createLineItem(*_) >> { arguments ->
            Long invoiceId = arguments?.get(0)
            LineItemResource lineItemResource = arguments?.get(1)

            assert invoiceId == INVOICE_ID
            assert lineItemResource.lineItem == createLineItemResource().lineItem
            assert lineItemResource.cost == createLineItemResource().cost

            LINE_ITEM_ID
        }
    }

    def "Delete a line item"() {
        given: "a valid invoice and valid line item"
        Long invoiceIdDeleteRequest = INVOICE_ID
        Long lineItemDeleteRequest = LINE_ITEM_ID

        when: "a new line item delete request is made"
        def request = delete("$INVOICE_URL/$invoiceIdDeleteRequest/line-items/$lineItemDeleteRequest")
        ResultActions response = mockMvc.perform(request)

        then: "a response is returned with a status of NO CONTENT (204)"
        response.andExpect(status().isNoContent())

        and: "the invoice service is called"
        1 * mockInvoiceService.deleteLineItem(_ as Long, _ as Long) >> { arguments ->
            Long invoiceId = arguments?.get(0)
            Long lineItemId = arguments?.get(1)

            assert invoiceId == INVOICE_ID
            assert lineItemId == LINE_ITEM_ID

            LINE_ITEM_ID
        }
    }

    def "Get all draft invoices"() {
        given: "a status"
        InvoiceStatus invoiceStatus = InvoiceStatus.draft

        when: "a new query request is made"
        def request = get("$INVOICE_URL?status=${invoiceStatus.name()}")
        ResultActions response = mockMvc.perform(request)

        then: "a response is returned with a status of OK (200)"
        response.andExpect(status().isOk())

        and: "the response body contains the error message"
        response.andExpect(content().json(loadJsonString("/json/invoices-get.json"), false))

        and: "the invoice service is called"
        1 * mockInvoiceService.getInvoices(_ as InvoiceStatus) >> { arguments ->
            InvoiceStatus status = arguments?.get(0)

            assert status == InvoiceStatus.draft

            getInvoices()
        }
    }

    private static InvoiceResource createInvoiceResource() {
        new InvoiceResource(
                "rob@site.com",
                "Robert Granger",
                "Sewer line replacement",
                LocalDate.parse("2021-12-30"),
                InvoiceStatus.draft,
                20000.00)
    }

    private static InvoiceResource createInvalidInvoiceResource() {
        new InvoiceResource(
                "rob@site.com",
                "Robert Granger",
                "Sewer line replacement",
                null,
                InvoiceStatus.draft,
                20000.00)
    }

    private static LineItemResource createLineItemResource() {
        new LineItemResource(
                "Remove old sink",
                20000.00
        )
    }

    private static LineItemResource createInvalidLineItemResource() {
        new LineItemResource()
    }

    private static List<Invoice> getInvoices() {
        [
                new Invoice(id: 1L,
                        customerEmail: "rob@site.com",
                        customerName: "Robert Granger",
                        description: "Bathroom renovation",
                        dueDate: LocalDate.of(2021, 12, 15),
                        status: InvoiceStatus.draft,
                        total: 21025.88,
                        lineItems: [
                                new LineItem(lineItemId: 1L,
                                        lineItem: "Demo existing bathroom",
                                        cost: 1500.00),
                                new LineItem(lineItemId: 2L,
                                        lineItem: "Replace sewer line",
                                        cost: 1500.00),
                                new LineItem(lineItemId: 3L,
                                        lineItem: "Replace toilet",
                                        cost: 500.00),
                                new LineItem(lineItemId: 4L,
                                        lineItem: "Replace bathtub",
                                        cost: 5000.00),
                                new LineItem(lineItemId: 5L,
                                        lineItem: "Replace vanity",
                                        cost: 1500.00)
                        ]
                ),
                new Invoice(id: 2L,
                        customerEmail: "joe.blow@site.com",
                        customerName: "Joe Blow",
                        description: "Sewer replacement",
                        dueDate: LocalDate.of(2021, 12, 31),
                        status: InvoiceStatus.draft,
                        total: 19800.00,
                        lineItems: [
                                new LineItem(lineItemId: 1L,
                                        lineItem: "Burst pipe replacement for sewer line from house to sidewalk",
                                        cost: 5000.00),
                                new LineItem(lineItemId: 2L,
                                        lineItem: "Reline sewer line from sidewalk to city main line",
                                        cost: 6500.00),
                                new LineItem(lineItemId: 3L,
                                        lineItem: "Replace house main sewer line",
                                        cost: 5500.00),
                                new LineItem(lineItemId: 4L,
                                        lineItem: "Replace all secondary lines to house main sewer line",
                                        cost: 2800.00)
                        ])
        ]
    }

    private String loadJsonString(String resourcePath) {
        URL url = getClass().getResource(resourcePath)
        Path path = Paths.get(url.toURI())
        Files.lines(path).collect(Collectors.joining())
    }
}
