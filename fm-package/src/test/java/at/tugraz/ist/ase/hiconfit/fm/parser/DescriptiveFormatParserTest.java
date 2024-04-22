/*
 * High Performance Knowledge Based Configuration Techniques
 *
 * Copyright (c) 2022-2024
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.hiconfit.fm.parser;

import at.tugraz.ist.ase.hiconfit.fm.core.AbstractRelationship;
import at.tugraz.ist.ase.hiconfit.fm.core.CTConstraint;
import at.tugraz.ist.ase.hiconfit.fm.core.Feature;
import at.tugraz.ist.ase.hiconfit.fm.core.FeatureModel;
import at.tugraz.ist.ase.hiconfit.fm.factory.FeatureModels;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class DescriptiveFormatParserTest {
    static FeatureModel<Feature, AbstractRelationship<Feature>, CTConstraint> featureModel;

    @Test
    void test() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/bamboobike.fm4conf");
//        @Cleanup("dispose")
//        FeatureModelParser<Feature, AbstractRelationship<Feature>, CTConstraint> parser = FMParserFactory.getInstance().getParser(fileFM.getName());
//        featureModel = parser.parse(fileFM);
        featureModel = FeatureModels.fromFile(fileFM);

        System.out.println(featureModel);

        String expected = """
                FEATURES:
                	Bamboo Bike
                	Frame
                	Brake
                	Engine
                	Drop Handlebar
                	Female
                	Male
                	Step-through
                	Front
                	Rear
                	Back-pedal
                RELATIONSHIPS:
                	mandatory(Bamboo Bike, Frame)
                	mandatory(Bamboo Bike, Brake)
                	optional(Bamboo Bike, Engine)
                	optional(Bamboo Bike, Drop Handlebar)
                	alternative(Frame, Female, Male, Step-through)
                	or(Brake, Front, Rear, Back-pedal)
                CONSTRAINTS:
                	excludes(Engine, Back-pedal)
                	requires(Drop Handlebar, Male)
                """;

        assertAll(() -> assertNotNull(featureModel),
                () -> assertEquals(expected, featureModel.toString()));
    }

    @Test
    void test2() throws FeatureModelParserException {
        File fileFM = new File("src/test/resources/REAL-FM-4.fm4conf");
        featureModel = FeatureModels.fromFile(fileFM);

        System.out.println(featureModel);

        String expected = """
                FEATURES:
                	eShop
                	Store front
                	Business management
                	Home page
                	Registration
                	Catalog
                	Wish list
                	Buy paths
                	Customer service
                	User behaviour tracking
                	Order management
                	Targeting
                	Affiliates
                	Inventory tracking
                	Procurement
                	Reporting and analysis
                	External systems integration
                	Administration
                	Static content
                	Dynamic content
                	Registration enforcement
                	Registration information
                	User behaviour tracking information
                	Product Information
                	Categories
                	Multiple catalogs
                	Searching
                	Browsing
                	Custom views
                	Wish list save after session
                	E-mail wish list
                	Multiple wish lists
                	Permissions
                	Shopping cart
                	Checkout
                	Order confirmation
                	Phone Ordering
                	Question and feedback forms
                	Product returns
                	Order status review
                	Shipment status tracking
                	Behaviour tracked
                	Fulfillment
                	Targeting criteria
                	Targeting mechanisms
                	Display and notification
                	Campaigns
                	Affiliate registration
                	Commission tracking
                	Allow backorders
                	Stock replenishment
                	Report types
                	Report formats
                	Level of detail
                	Fulfillment system
                	Inventory management system
                	Procurement system
                	External distributor system
                	Content management
                	Store administration
                	Content type
                	Variation source
                	Register to browse
                	Register to buy
                	None
                	Login credentials
                	Shipping address
                	Billing address
                	Credit card information
                	Demographics
                	Personal Information
                	Preferences
                	Reminders
                	Quick checkout profile
                	Custom fields
                	Product type
                	Basic information
                	Detailed information
                	Warranty information
                	Customer reviews
                	Associated assets
                	Product variants
                	Size
                	Weight
                	Availability
                	Custom fields1
                	Catalog1
                	Basic search
                	Advanced search
                	Product page
                	Category page
                	Index page
                	Seasonal product views
                	Personalized views
                	Public access
                	Restricted access
                	Private access
                	Inventory management policy
                	Cart content page
                	Cart summary page
                	Cart save after session
                	Checkout type
                	Shipping options
                	Taxation options
                	Payment options
                	Eletronic page
                	E-mail
                	Phone
                	Mail
                	Digital Dialing
                	Rotary Dialing
                	Question and feedback tracking
                	Filtering criteria
                	Request order hardcopy
                	Internal tracking
                	Partner tracking
                	Locally visited pages
                	External referring pages
                	Previous purchases1
                	Physical goods fulfillment
                	Eletronic goods fulfillment
                	Services fulfillment
                	Customer preferences
                	Personal information
                	Demographics1
                	Previous purchases
                	Shopping cart content
                	Wish list content
                	Previously visited pages
                	Date and time
                	Custom target criteria
                	Advertisements
                	Discounts
                	Sell strategies
                	Assignment to page types for display
                	Product flagging
                	E-mails
                	Manual
                	Automatic
                	Product database management
                	Presentation options
                	General layout management
                	Content approval
                	Site search
                	Search engine registration
                	Domain name setup
                	Welcome message
                	Special offers
                	Time dependent
                	Personalized
                	Multiple shipping addresses
                	Multiple billing addresses
                	Card holder name
                	Card number
                	Expiry date
                	Security information
                	Age
                	Income
                	Education
                	Custom Demographic field
                	Site layout
                	List size
                	Language
                	Eletronic goods
                	Physical goods
                	Services
                	Documents
                	Media files
                	Complex product configuration
                	Categories1
                	Sorting filters
                	Registered checkout
                	Guest checkout
                	Quality of service selection
                	Carrier selection
                	Gift options
                	Multiple shipments
                	Shipping cost calculation
                	Custom taxation
                	Tax gateways
                	Payment types
                	Fraud detection
                	Payment gateways
                	Order number
                	Order date
                	Order status
                	Warehouse management
                	Shipping1
                	File repository
                	License management
                	Appointment scheduling
                	Resource planning
                	Advertisement types
                	Advertisement sources
                	Advertisement response tracking
                	Context sensitive ads
                	Discount conditions
                	Award
                	Eligibility requirements
                	Graduation by
                	Coupons
                	Handling multiple discounts
                	Product kitting
                	Up-selling
                	Cross-selling
                	Personalized1
                	Response tracking
                	Non-repudiation service
                	Image
                	Video
                	Sound
                	Multi-level
                	Multiple classification
                	Price
                	Quality
                	Price-Quality ratio
                	Manufacturer name
                	Custom filter
                	Quick checkout
                	Type
                	Ammount specification
                	CertiTAX
                	CyberSource
                	Custom tax gateway
                	COD
                	Credit card
                	Debit card
                	Eletronic cheque
                	Fax mail order
                	Purchase order
                	Gift certificate
                	Phone order
                	Custom payment type
                	Authorize.Net
                	CyberSource1
                	LinkPoint
                	Paradata
                	SkipJack
                	Verisign Payflow Pro
                	Custom payment gateway
                	Custom shipping method
                	Shipping gateways
                	Banner ads
                	Pop-up ads
                	House advertisements
                	Paid advertisements
                	Product and quantity scope
                	Time scope
                	Purchase value scope
                	Percentage discount
                	Fixed discount
                	Free item
                	Customer segments
                	Shipping address1
                	Purchase value
                	Quantity
                	Substitute products
                	Past customers also bought
                	Thumbnail
                	2D image
                	3D image
                	360 degrees image
                	Different perspectives
                	Gallery
                	Enable profile update on checkout
                	Fixed-rate taxation
                	Rule-based taxation
                	Surcharge
                	Percentage
                	Pricing
                	FedEX
                	UPS
                	USPS
                	Canada Post
                	Custom shipping gateway
                	Advertisement management interface
                	Tax codes
                	Address
                	Resolution
                	Flat rate
                	Rate factors
                	Shipping
                	Billing
                	Country
                	Region
                	City
                	Quantity purchased
                	Order total
                	Weight1
                	Product classification
                RELATIONSHIPS:
                	mandatory(eShop, Store front)
                	mandatory(eShop, Business management)
                	optional(Store front, Home page)
                	optional(Store front, Registration)
                	mandatory(Store front, Catalog)
                	optional(Store front, Wish list)
                	mandatory(Store front, Buy paths)
                	optional(Store front, Customer service)
                	optional(Store front, User behaviour tracking)
                	mandatory(Business management, Order management)
                	optional(Business management, Targeting)
                	optional(Business management, Affiliates)
                	optional(Business management, Inventory tracking)
                	optional(Business management, Procurement)
                	optional(Business management, Reporting and analysis)
                	optional(Business management, External systems integration)
                	mandatory(Business management, Administration)
                	or(Home page, Static content, Dynamic content)
                	mandatory(Registration, Registration enforcement)
                	mandatory(Registration, Registration information)
                	optional(Registration, User behaviour tracking information)
                	mandatory(Catalog, Product Information)
                	optional(Catalog, Categories)
                	optional(Catalog, Multiple catalogs)
                	optional(Catalog, Searching)
                	optional(Catalog, Browsing)
                	optional(Catalog, Custom views)
                	optional(Wish list, Wish list save after session)
                	optional(Wish list, E-mail wish list)
                	optional(Wish list, Multiple wish lists)
                	optional(Wish list, Permissions)
                	mandatory(Buy paths, Shopping cart)
                	mandatory(Buy paths, Checkout)
                	mandatory(Buy paths, Order confirmation)
                	or(Buy paths, Phone Ordering)
                	or(Customer service, Question and feedback forms, Product returns, Order status review, Shipment status tracking)
                	mandatory(User behaviour tracking, Behaviour tracked)
                	mandatory(Order management, Fulfillment)
                	mandatory(Targeting, Targeting criteria)
                	mandatory(Targeting, Targeting mechanisms)
                	mandatory(Targeting, Display and notification)
                	optional(Targeting, Campaigns)
                	mandatory(Affiliates, Affiliate registration)
                	mandatory(Affiliates, Commission tracking)
                	optional(Inventory tracking, Allow backorders)
                	mandatory(Procurement, Stock replenishment)
                	mandatory(Reporting and analysis, Report types)
                	mandatory(Reporting and analysis, Report formats)
                	mandatory(Reporting and analysis, Level of detail)
                	or(External systems integration, Fulfillment system, Inventory management system, Procurement system, External distributor system)
                	mandatory(Administration, Content management)
                	mandatory(Administration, Store administration)
                	or(Registration enforcement, Register to browse, Register to buy, None)
                	mandatory(Registration information, Login credentials)
                	optional(Registration information, Shipping address)
                	optional(Registration information, Billing address)
                	optional(Registration information, Credit card information)
                	optional(Registration information, Demographics)
                	optional(Registration information, Personal Information)
                	optional(Registration information, Preferences)
                	optional(Registration information, Reminders)
                	optional(Registration information, Quick checkout profile)
                	optional(Registration information, Custom fields)
                	mandatory(Product Information, Product type)
                	mandatory(Product Information, Basic information)
                	optional(Product Information, Detailed information)
                	optional(Product Information, Warranty information)
                	optional(Product Information, Customer reviews)
                	optional(Product Information, Associated assets)
                	optional(Product Information, Product variants)
                	optional(Product Information, Size)
                	optional(Product Information, Weight)
                	optional(Product Information, Availability)
                	optional(Product Information, Custom fields1)
                	mandatory(Categories, Catalog1)
                	or(Searching, Basic search, Advanced search)
                	mandatory(Browsing, Product page)
                	optional(Browsing, Category page)
                	optional(Browsing, Index page)
                	optional(Custom views, Seasonal product views)
                	optional(Custom views, Personalized views)
                	or(Permissions, Public access, Restricted access, Private access)
                	mandatory(Shopping cart, Inventory management policy)
                	mandatory(Shopping cart, Cart content page)
                	optional(Shopping cart, Cart summary page)
                	optional(Shopping cart, Cart save after session)
                	mandatory(Checkout, Checkout type)
                	optional(Checkout, Shipping options)
                	mandatory(Checkout, Taxation options)
                	mandatory(Checkout, Payment options)
                	or(Order confirmation, Eletronic page, E-mail, Phone, Mail)
                	or(Behaviour tracked, Locally visited pages, External referring pages, Previous purchases1)
                	or(Fulfillment, Physical goods fulfillment, Eletronic goods fulfillment, Services fulfillment)
                	or(Targeting criteria, Customer preferences, Personal information, Demographics1, Previous purchases, Shopping cart content, Wish list content, Previously visited pages, Date and time, Custom target criteria)
                	or(Targeting mechanisms, Advertisements, Discounts, Sell strategies)
                	or(Display and notification, Assignment to page types for display, Product flagging, E-mails)
                	mandatory(Stock replenishment, Manual)
                	optional(Stock replenishment, Automatic)
                	mandatory(Content management, Product database management)
                	mandatory(Content management, Presentation options)
                	mandatory(Content management, General layout management)
                	optional(Content management, Content approval)
                	mandatory(Store administration, Site search)
                	mandatory(Store administration, Search engine registration)
                	mandatory(Store administration, Domain name setup)
                	mandatory(Dynamic content, Content type)
                	mandatory(Dynamic content, Variation source)
                	optional(Shipping address, Multiple shipping addresses)
                	optional(Billing address, Multiple billing addresses)
                	mandatory(Credit card information, Card holder name)
                	mandatory(Credit card information, Card number)
                	mandatory(Credit card information, Expiry date)
                	optional(Credit card information, Security information)
                	or(Demographics, Age, Income, Education, Custom Demographic field)
                	or(Preferences, Site layout, List size, Language)
                	or(Product type, Eletronic goods, Physical goods, Services)
                	or(Associated assets, Documents, Media files)
                	optional(Product variants, Complex product configuration)
                	optional(Catalog1, Categories1)
                	optional(Index page, Sorting filters)
                	or(Checkout type, Registered checkout, Guest checkout)
                	optional(Shipping options, Quality of service selection)
                	optional(Shipping options, Carrier selection)
                	optional(Shipping options, Gift options)
                	optional(Shipping options, Multiple shipments)
                	mandatory(Shipping options, Shipping cost calculation)
                	or(Taxation options, Custom taxation, Tax gateways)
                	mandatory(Payment options, Payment types)
                	optional(Payment options, Fraud detection)
                	optional(Payment options, Payment gateways)
                	mandatory(Phone Ordering, Digital Dialing)
                	optional(Phone Ordering, Rotary Dialing)
                	optional(Question and feedback forms, Question and feedback tracking)
                	mandatory(Order status review, Filtering criteria)
                	optional(Order status review, Request order hardcopy)
                	or(Shipment status tracking, Internal tracking, Partner tracking)
                	mandatory(Automatic, Non-repudiation service)
                	or(Content type, Welcome message, Special offers)
                	or(Variation source, Time dependent, Personalized)
                	optional(Categories1, Multi-level)
                	optional(Categories1, Multiple classification)
                	or(Sorting filters, Price, Quality, Price-Quality ratio, Manufacturer name, Custom filter)
                	or(Payment types, COD, Credit card, Debit card, Eletronic cheque, Fax mail order, Purchase order, Gift certificate, Phone order, Custom payment type)
                	or(Payment gateways, Authorize.Net, CyberSource1, LinkPoint, Paradata, SkipJack, Verisign Payflow Pro, Custom payment gateway)
                	or(Filtering criteria, Order number, Order date, Order status)
                	mandatory(Physical goods fulfillment, Warehouse management)
                	mandatory(Physical goods fulfillment, Shipping1)
                	mandatory(Eletronic goods fulfillment, File repository)
                	mandatory(Eletronic goods fulfillment, License management)
                	optional(Services fulfillment, Appointment scheduling)
                	optional(Services fulfillment, Resource planning)
                	mandatory(Advertisements, Advertisement types)
                	mandatory(Advertisements, Advertisement sources)
                	optional(Advertisements, Advertisement response tracking)
                	optional(Advertisements, Context sensitive ads)
                	mandatory(Discounts, Discount conditions)
                	mandatory(Discounts, Award)
                	mandatory(Discounts, Eligibility requirements)
                	mandatory(Discounts, Graduation by)
                	optional(Discounts, Coupons)
                	mandatory(Discounts, Handling multiple discounts)
                	or(Sell strategies, Product kitting, Up-selling, Cross-selling)
                	optional(E-mails, Personalized1)
                	optional(E-mails, Response tracking)
                	or(Media files, Image, Video, Sound)
                	optional(Registered checkout, Quick checkout)
                	mandatory(Custom taxation, Type)
                	mandatory(Custom taxation, Ammount specification)
                	or(Tax gateways, CertiTAX, CyberSource, Custom tax gateway)
                	or(Shipping1, Custom shipping method, Shipping gateways)
                	or(Advertisement types, Banner ads, Pop-up ads)
                	or(Advertisement sources, House advertisements, Paid advertisements)
                	mandatory(Discount conditions, Product and quantity scope)
                	mandatory(Discount conditions, Time scope)
                	optional(Discount conditions, Purchase value scope)
                	or(Award, Percentage discount, Fixed discount, Free item)
                	optional(Eligibility requirements, Customer segments)
                	optional(Eligibility requirements, Shipping address1)
                	or(Graduation by, Purchase value, Quantity)
                	optional(Quick checkout, Enable profile update on checkout)
                	or(Type, Fixed-rate taxation, Rule-based taxation)
                	or(Ammount specification, Surcharge, Percentage)
                	mandatory(Up-selling, Substitute products)
                	mandatory(Cross-selling, Past customers also bought)
                	or(Image, Thumbnail, 2D image, 3D image, 360 degrees image, Different perspectives, Gallery)
                	mandatory(Custom shipping method, Pricing)
                	or(Shipping gateways, FedEX, UPS, USPS, Canada Post, Custom shipping gateway)
                	mandatory(Paid advertisements, Advertisement management interface)
                	mandatory(Rule-based taxation, Tax codes)
                	mandatory(Rule-based taxation, Address)
                	optional(Rule-based taxation, Resolution)
                	mandatory(Pricing, Flat rate)
                	optional(Pricing, Rate factors)
                	mandatory(Address, Shipping)
                	optional(Address, Billing)
                	or(Resolution, Country, Region, City)
                	or(Rate factors, Quantity purchased, Order total, Weight1, Product classification)
                CONSTRAINTS:
                	requires(Wish list, Wish list save after session)
                	requires(Registration, Wish list save after session)
                	requires(Eletronic goods, Size)
                	requires(Physical goods, Size)
                	requires(Eletronic goods, Eletronic goods fulfillment)
                	requires(User behaviour tracking information, User behaviour tracking)
                	requires(Quick checkout, Quick checkout profile)
                	requires(Customer preferences, Preferences)
                	requires(Services, Services fulfillment)
                	requires(Physical goods, Physical goods fulfillment)
                	requires(Wish list content, Wish list)
                	requires(Registered checkout, Register to buy)
                	requires(Shipping options, Shipping1)
                	requires(Registered checkout, Registration enforcement)
                	((~Previously visited pages \\/ Locally visited pages) \\/ External referring pages)
                	requires(Special offers, Discounts)
                	requires(Availability, Inventory tracking)
                	requires(Physical goods, Weight)
                	requires(Category page, Categories)
                	requires(Permissions, Registration)
                	requires(E-mail wish list, Registration)
                """;

        assertAll(() -> assertNotNull(featureModel),
                () -> assertEquals(expected, featureModel.toString()));
    }
}