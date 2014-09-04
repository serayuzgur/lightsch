<?xml version="1.0" encoding="UTF-8"?>
<schema xmlns="http://purl.oclc.org/dsdl/schematron" 
		xmlns:sch="http://purl.oclc.org/dsdl/schematron" 
		xmlns:xs="http://www.w3.org/2001/XMLSchema" 
		queryBinding="xslt2">

	<ns prefix="gl-plt" uri="http://www.xbrl.org/int/gl/plt/2010-04-16" />
	<ns prefix="gl-cor" uri="http://www.xbrl.org/int/gl/cor/2006-10-25" />
	<ns prefix="gl-bus" uri="http://www.xbrl.org/int/gl/bus/2006-10-25" />
	<ns prefix="xbrli" uri="http://www.xbrl.org/2003/instance" />
	<ns prefix="ds" uri="http://www.w3.org/2000/09/xmldsig#" />
	<ns prefix="xades" uri="http://uri.etsi.org/01903/v1.3.2#" />
	<ns prefix="edefter" uri="http://www.edefter.gov.tr" />
	
	<title>Yevmiye defteri dokumanlarını kontrol etmek için gerekli olan schematron kuralları</title>
	
	<let name="periodCoveredStart" value="/edefter:defter/xbrli:xbrl/gl-cor:accountingEntries/gl-cor:documentInfo/gl-cor:periodCoveredStart"/>
	<let name="periodCoveredEnd" value="/edefter:defter/xbrli:xbrl/gl-cor:accountingEntries/gl-cor:documentInfo/gl-cor:periodCoveredEnd"/>
	<let name="vkn" value="/edefter:defter/xbrli:xbrl/xbrli:context/xbrli:entity/xbrli:identifier"/>
	
	<!-- Yevmiye defterinin ana(kök) elemanı <edefter:defter> olmalıdır. -->
	<pattern id="kok">
		<rule context="*">
			<assert test="not(@decimals) or  @decimals = 'INF'">Belge içerisindeki 'decimals' nitelikleri 'INF' değerini almalıdır.</assert>
		</rule>
		<rule context="/">
			<assert test="edefter:defter">Yevmiye defteri dokumanı edefter:defter ana elemanı ile başlamalıdır.</assert>
		</rule>
	</pattern>

	<!-- <xbrli:xbrl> elemanı bir <xbrli:context>, en az bir <xbrli:unit> ve bir <gl-cor:accountingEntries> elmanına sahip olmalıdır -->
	<pattern id="xbrl">
		<rule context="/edefter:defter/xbrli:xbrl">
			<assert test="count(xbrli:context) = 1 ">xbrli:context zorunlu bir elemandır.</assert>
			<assert test="count(xbrli:unit) >= 1 ">xbrli:unit zorunlu bir elemandır.</assert>
			<assert test="count(gl-cor:accountingEntries) = 1 ">gl-cor:accountingEntries zorunlu bir elemandır.</assert>
		</rule>
		<rule context="/edefter:defter/xbrli:xbrl/xbrli:context/xbrli:entity/xbrli:identifier">
			<assert test="matches(normalize-space(.) , '^[0-9]{10,11}$')">xbrli:identifier elemanına 10 haneli vergi kimlik numarası ve ya 11 haneli TC kimlik numarası yazılmalıdır.</assert>
		</rule>		
	</pattern>
	
	<!-- <gl-cor:accountingEntries> elemanı bir <gl-cor:entityInformation> elemanı içermelidir.  -->
	<pattern id="accountingentries">
		<rule context="/edefter:defter/xbrli:xbrl/gl-cor:accountingEntries">
			<assert test="gl-cor:documentInfo">gl-cor:documentInfo zorunlu bir elemandır.</assert>
			<assert test="gl-cor:entityInformation">gl-cor:entityInformation zorunlu bir elemandır.</assert>
		</rule>		
	</pattern>
	
	<!--  <gl-cor:documentInfo> kontrolleri -->
	<pattern id="documentinfo">
		<rule context="/edefter:defter/xbrli:xbrl/gl-cor:accountingEntries/gl-cor:documentInfo">
			<assert test="gl-cor:entriesType">gl-cor:entriesType zorunlu bir elemandır.</assert>
			<assert test="normalize-space(gl-cor:entriesType) = 'journal'">gl-cor:entriesType elemanı yevmiye defteri için 'journal' değerini almalıdır.</assert>
			
			<assert test="gl-cor:uniqueID">gl-cor:uniqueID zorunlu bir elemandır.</assert>
			<assert test="not(gl-cor:uniqueID) or starts-with(normalize-space(gl-cor:uniqueID),'YEV')">gl-cor:uniqueID elemanı yevmiye defteri için 'YEV' değeri ile başlamalıdır.</assert>
			<assert test="not(gl-cor:uniqueID) or string-length(normalize-space(gl-cor:uniqueID)) = 11 or string-length(normalize-space(gl-cor:uniqueID)) = 13 ">gl-cor:uniqueID elemanı 13 karakterden oluşmalıdır.</assert>
			
			<assert test="gl-cor:creationDate">gl-cor:creationDate zorunlu bir elemandır.</assert>
			
			<assert test="gl-cor:periodCoveredStart">gl-cor:periodCoveredStart zorunlu bir elemandır.</assert>
			<assert test="gl-cor:periodCoveredEnd">gl-cor:periodCoveredEnd zorunlu bir elemandır.</assert>
			<assert test="gl-cor:periodCoveredEnd >= gl-cor:periodCoveredStart">gl-cor:periodCoveredEnd elemanı gl-cor:periodCoveredStart elemanından büyük ve ya eşit olmalıdır.</assert>
			
			<assert test="gl-cor:creationDate >= gl-cor:periodCoveredEnd">gl-cor:creationDate elemanı gl-cor:periodCoveredEnd elemanından büyük ve ya eşit olmalıdır.</assert>
			
			<assert test="string-length(normalize-space(gl-bus:sourceApplication)) > 0">gl-bus:sourceApplication zorunlu bir elemandır ve değeri boşluk olmamalıdır.</assert>
		</rule>
	</pattern>
	
	<!-- <gl-cor:entityInformation> kontrolleri -->
	<pattern id="entityinformation">
		<rule context="/edefter:defter/xbrli:xbrl/gl-cor:accountingEntries/gl-cor:entityInformation">
			<assert test="gl-bus:entityPhoneNumber">gl-bus:entityPhoneNumber zorunlu bir elemandır.</assert>
						
			<assert test="gl-bus:entityEmailAddressStructure">gl-bus:entityEmailAddressStructure zorunlu bir elemandır.</assert>
			
			<assert test="count(gl-bus:organizationIdentifiers) >= 1">gl-bus:organizationIdentifiers zorunlu bir elemandır.</assert>
			<assert test="not(string-length($vkn) = 10) or count(gl-bus:organizationIdentifiers[gl-bus:organizationDescription = 'Kurum Unvanı']) = 1">gl-bus:organizationDescription değeri 'Kurum Unvanı' olan bir tane gl-bus:organizationIdentifiers elemanı bulunmalıdır.</assert>
			<assert test="not(string-length($vkn) = 11) or count(gl-bus:organizationIdentifiers[gl-bus:organizationDescription = 'Adı Soyadı']) = 1">gl-bus:organizationDescription değeri 'Adı Soyadı' olan bir tane gl-bus:organizationIdentifiers elemanı bulunmalıdır.</assert>

			<let name="countKurumUnvani" value="count(gl-bus:organizationIdentifiers[gl-bus:organizationDescription = 'Kurum Unvanı'])"/>
			<let name="countAdiSoyadi" value="count(gl-bus:organizationIdentifiers[gl-bus:organizationDescription = 'Adı Soyadı'])"/>
			
			<assert test="($countKurumUnvani=1 and not($countAdiSoyadi=1)) or ($countAdiSoyadi=1 and not($countKurumUnvani=1))">gl-bus:organizationDescription değeri 'Kurum Unvanı' veya 'Adı Soyadı' olan yalnızca bir tane gl-bus:organizationIdentifiers elemanı bulunmalıdır.</assert>
			
			<assert test="not(count(gl-bus:organizationIdentifiers[gl-bus:organizationDescription = 'Kurum Unvanı']) = 1) or 
				string-length(normalize-space(gl-bus:organizationIdentifiers[gl-bus:organizationDescription = 'Kurum Unvanı']/gl-bus:organizationIdentifier)) >=2">gl-bus:organizationDescription değeri 'Kurum Unvanı' olan gl-bus:organizationIdentifiers elemanının  gl-bus:organizationIdentifier eleman değeri en az iki karakter olmalıdır.</assert>
			<assert test="not(count(gl-bus:organizationIdentifiers[gl-bus:organizationDescription = 'Adı Soyadı']) = 1) or 
				string-length(normalize-space(gl-bus:organizationIdentifiers[gl-bus:organizationDescription = 'Adı Soyadı']/gl-bus:organizationIdentifier)) >=2">gl-bus:organizationDescription değeri 'Adı Soyadı' olan gl-bus:organizationIdentifiers elemanının  gl-bus:organizationIdentifier eleman değeri en az iki karakter olmalıdır.</assert>
			
			<let name="countSubeNo" value="count(gl-bus:organizationIdentifiers[gl-bus:organizationDescription = 'Şube No'])"/>
			<let name="countSubeAdi" value="count(gl-bus:organizationIdentifiers[gl-bus:organizationDescription = 'Şube Adı'])"/>
			
			<assert test="(not($countSubeNo = 1) or $countSubeAdi = 1) and (not($countSubeAdi = 1) or $countSubeNo = 1)">Şube no ve şube adı birlikte bulunmalıdır.</assert>

			<assert test="($countSubeNo &lt; 2) and ($countSubeAdi &lt; 2)">Şube no veya şube adı birden fazla olamaz.</assert>
			
			<assert test="not($countSubeNo = 1) or matches(normalize-space(gl-bus:organizationIdentifiers[gl-bus:organizationDescription = 'Şube No']/gl-bus:organizationIdentifier) , '^[0-9]{4}$')">Şube no 4 haneli sayısal bir değer olmalıdır.</assert>
					
			<assert test="not($countSubeAdi = 1) or string-length(normalize-space(gl-bus:organizationIdentifiers[gl-bus:organizationDescription = 'Şube Adı']/gl-bus:organizationIdentifier)) >= 2">Şube adı değeri en az iki karakter olmalıdır.</assert>			
			
			<assert test="gl-bus:organizationAddress">gl-bus:organizationAddress zorunlu bir elemandır.</assert>
			<assert test="not(gl-bus:organizationAddress) or gl-bus:organizationAddress/gl-bus:organizationBuildingNumber">gl-bus:organizationAddress elemanı içerisindeki gl-bus:organizationBuildingNumber zorunlu bir elemandır.</assert>
			<assert test="not(gl-bus:organizationAddress) or gl-bus:organizationAddress/gl-bus:organizationAddressStreet">gl-bus:organizationAddress elemanı içerisindeki gl-bus:organizationAddressStreet zorunlu bir elemandır.</assert>
			<assert test="not(gl-bus:organizationAddress) or gl-bus:organizationAddress/gl-bus:organizationAddressCity">gl-bus:organizationAddress elemanı içerisindeki gl-bus:organizationAddressCity zorunlu bir elemandır.</assert>
			<assert test="not(gl-bus:organizationAddress) or gl-bus:organizationAddress/gl-bus:organizationAddressZipOrPostalCode">gl-bus:organizationAddress' elemanı içerisindeki 'gl-bus:organizationAddressZipOrPostalCode zorunlu bir elemandır.</assert>
			<assert test="not(gl-bus:organizationAddress) or gl-bus:organizationAddress/gl-bus:organizationAddressCountry">gl-bus:organizationAddress elemanı içerisindeki gl-bus:organizationAddressCountry zorunlu bir elemandır.</assert>

			<assert test="gl-bus:entityWebSite">gl-bus:entityWebSite zorunlu bir elemandır.</assert>			
			
			<assert test="string-length(normalize-space(gl-bus:businessDescription)) > 0">gl-bus:businessDescription zorunlu bir elemandır ve değeri boşluk olmamalıdır.</assert>
			
			<assert test="gl-bus:fiscalYearStart">gl-bus:fiscalYearStart zorunlu bir elemandır.</assert>			
			<assert test="gl-bus:fiscalYearEnd">gl-bus:fiscalYearEnd zorunlu bir elemandır.</assert>
			<assert test="gl-bus:fiscalYearEnd > gl-bus:fiscalYearStart">gl-bus:fiscalYearEnd elemanı gl-bus:fiscalYearStart elemanından büyük olmalıdır.</assert>
			
			<assert test="gl-bus:accountantInformation">gl-bus:accountantInformation zorunlu bir elemandır.</assert>			
		</rule>
		<rule context="/edefter:defter/xbrli:xbrl/gl-cor:accountingEntries/gl-cor:entityInformation/gl-bus:accountantInformation">
			<assert test="string-length(normalize-space(gl-bus:accountantName)) > 0">gl-bus:accountantInformation elemanı içerisindeki gl-bus:accountantName zorunlu bir elemandır ve değeri boşluk olmamalıdır.</assert>
			<assert test="string-length(normalize-space(gl-bus:accountantEngagementTypeDescription)) > 0">gl-bus:accountantInformation elemanı içerisindeki gl-bus:accountantEngagementTypeDescription zorunlu bir elemandır  ve değeri boşluk olmamalıdır.</assert>
		</rule>
		<rule context="/edefter:defter/xbrli:xbrl/gl-cor:accountingEntries/gl-cor:entityInformation/gl-bus:entityPhoneNumber">
			<assert test="string-length(normalize-space(gl-bus:phoneNumber)) > 0">gl-bus:phoneNumber zorunlu bir elemandır ve değeri boşluk olmamalıdır.</assert>			
		</rule>
		<rule context="/edefter:defter/xbrli:xbrl/gl-cor:accountingEntries/gl-cor:entityInformation/gl-bus:entityEmailAddressStructure">
			<assert test="string-length(normalize-space(gl-bus:entityEmailAddress)) > 0">gl-bus:entityEmailAddressStructure elemanı içerisinde gl-bus:entityEmailAddress zorunlu bir elemandır ve  ve değeri boşluk olmamalıdır.</assert>		
		</rule>
		<rule context="/edefter:defter/xbrli:xbrl/gl-cor:accountingEntries/gl-cor:entityInformation/gl-bus:entityWebSite">
			<assert test="gl-bus:webSiteURL">gl-bus:entityWebSite elemanı içerisindeki gl-bus:webSiteURL zorunlu bir elemandır.</assert>
		</rule>
	</pattern>
	
	<!-- <gl-cor:entryHeader> kontrolleri -->
	<pattern id="entryheader">
		<rule context="/edefter:defter/xbrli:xbrl/gl-cor:accountingEntries/gl-cor:entryHeader">
			<assert test="not(following::node()) or not(following::node()/gl-cor:entryNumberCounter) or not(gl-cor:entryNumberCounter) or following::node()/gl-cor:entryNumberCounter = gl-cor:entryNumberCounter + 1" >gl-cor:entryNumberCounter müteselsil bir değere sahip olmalıdır.</assert>
			
			<assert test="not(following::node()) or not(following::node()/gl-cor:enteredDate) or not(gl-cor:enteredDate) or following::node()/gl-cor:enteredDate >= gl-cor:enteredDate">Yevmiye defteri tarihe göre sıralı olmalıdır.</assert>
			
			<assert test="gl-cor:enteredBy">gl-cor:enteredBy zorunlu bir elemandır.</assert>
			<assert test="not(gl-cor:enteredBy) or string-length(normalize-space(gl-cor:enteredBy)) >= 2">gl-cor:enteredBy elemanı en az iki karakter olmamalıdır.</assert>
			
			<assert test="gl-cor:enteredDate">gl-cor:enteredDate zorunlu bir elemandır.</assert>
			<assert test="not(gl-cor:enteredDate) or (gl-cor:enteredDate >= $periodCoveredStart and gl-cor:enteredDate &lt;= $periodCoveredEnd)">gl-cor:enteredDate elemanın değeri <value-of select="$periodCoveredStart"/> ile <value-of select="$periodCoveredEnd"/> değerleri arasında olmalıdır.</assert>
			
			<assert test="gl-bus:totalDebit">gl-bus:totalDebit zorunlu bir elemandır.</assert>	
			<assert test="gl-bus:totalCredit">gl-bus:totalCredit zorunlu bir elemandır.</assert>
			<assert test="gl-cor:entryNumberCounter">gl-cor:entryNumberCounter zorunlu bir elemandır.</assert>
			<assert test="count(gl-cor:entryDetail) > 1">gl-cor:entryHeader elemanı en az iki gl-cor:entryDetail elemanı içermelidir.</assert>
			
			<assert test="gl-bus:totalDebit = gl-bus:totalCredit">gl-bus:totalDebit elemanının değeri gl-bus:totalCredit elemanının değerine eşit olmalıdır.</assert> 
			<assert test="gl-bus:totalDebit = sum(gl-cor:entryDetail[gl-cor:debitCreditCode = 'D' or gl-cor:debitCreditCode = 'debit']/xs:decimal(gl-cor:amount))">gl-bus:totalDebit değeri, gl-cor:entryDetail elemanı içerisindeki gl-cor:debitCreditCode değeri 'D' ve ya 'debit' olan gl-cor:amount değelerinin toplamına eşit olmalıdır.</assert>
			<assert test="gl-bus:totalCredit = sum(gl-cor:entryDetail[gl-cor:debitCreditCode = 'C' or gl-cor:debitCreditCode = 'credit']/xs:decimal(gl-cor:amount))">gl-bus:totalCredit değeri, gl-cor:entryDetail elemanı içerisindeki gl-cor:debitCreditCode değeri 'C' ve ya 'credit' olan gl-cor:amount değerlerinin toplamına eşit olmalıdır.</assert>
			
		</rule>
	</pattern>

	<!-- <gl-cor:entryDetail> kontrolleri -->
	<pattern id="entrydetail">
		<rule context="/edefter:defter/xbrli:xbrl/gl-cor:accountingEntries/gl-cor:entryHeader/gl-cor:entryDetail">
			<assert test="gl-cor:lineNumber">gl-cor:lineNumber zorunlu bir elemandır.</assert>
			<assert test="not(following::node()) or not(following::node()/gl-cor:lineNumber) or not(gl-cor:lineNumber) or following::node()/number(gl-cor:lineNumber) = number(gl-cor:lineNumber) + 1" >gl-cor:lineNumber müteselsil bir değere sahip olmalıdır.</assert>
			
			<assert test="gl-cor:lineNumberCounter">gl-cor:lineNumberCounter zorunlu bir elemandır.</assert>			
			<assert test="not(gl-cor:lineNumberCounter) or not(parent::node()/gl-cor:entryNumberCounter) or gl-cor:lineNumberCounter = parent::node()/gl-cor:entryNumberCounter">gl-cor:lineNumberCounter elemanının değeri bir üst düzeydeki gl-cor:entryNumberCounter elemanının değerine eşit olmalıdır.</assert>
			
			<assert test="gl-cor:account">gl-cor:account zorunlu bir elemandır.</assert>
			<assert test="not(gl-cor:account) or not(gl-cor:account/gl-cor:accountMainID) or string-length(gl-cor:account/normalize-space(gl-cor:accountMainID)) = 3 or string-length(gl-cor:account/normalize-space(gl-cor:accountMainID)) = 4">gl-cor:account elemanı içerisinde gl-cor:accountMainID zorunlu bir elemandır ve en az 3 karakter olmalıdır.</assert>
			<assert test="not(gl-cor:account) or gl-cor:account/gl-cor:accountMainDescription">gl-cor:account elemanı içerisinde gl-cor:accountMainDescription zorunlu bir elemandır.</assert>
			<assert test="not(gl-cor:account/gl-cor:accountSub) or gl-cor:account/gl-cor:accountSub/gl-cor:accountSubID">gl-cor:accountSub elemanı içerisinde gl-cor:accountSubID zorunlu bir elemandır.</assert>			
			<assert test="not(gl-cor:account/gl-cor:accountSub) or gl-cor:account/gl-cor:accountSub/gl-cor:accountSubDescription">gl-cor:accountSub elemanı içerisinde gl-cor:accountSubDescription zorunlu bir elemandır.</assert>
			
			<let name="anaHesapId" value="gl-cor:account/normalize-space(gl-cor:accountMainID)"/>
			<let name="altHesapId" value="gl-cor:account/normalize-space(gl-cor:accountSub/gl-cor:accountSubID)"/>
			<assert test="not($anaHesapId) or not($altHesapId) or starts-with($altHesapId, $anaHesapId)">gl-cor:accountSubID(alt hesap numarası) elemanı gl-cor:accountMainID(ana hesap numarası) değeri ile başlamalıdır.</assert>
			
			<assert test="gl-cor:amount">gl-cor:amount zorunlu bir elemandır.</assert>
			<assert test="not(gl-cor:amount) or gl-cor:amount > 0">gl-cor:amount elemanı 0'dan büyük bir değer almalıdır.</assert>
			
			<assert test="gl-cor:debitCreditCode">gl-cor:debitCreditCode zorunlu bir elemandır.</assert>	
			
			<assert test="gl-cor:postingDate">gl-cor:postingDate zorunlu bir elemandır.</assert>
			<assert test="not(gl-cor:postingDate) or not(parent::node()/gl-cor:enteredDate) or gl-cor:postingDate = parent::node()/gl-cor:enteredDate">gl-cor:postingDate elemanının değeri bir üst düzeydeki gl-cor:enteredDate elemanının değerine eşit olmalıdır.</assert>
			
			<assert test="not(normalize-space(gl-cor:documentType) = 'other') or string-length(normalize-space(gl-cor:documentTypeDescription)) > 0">Yevmiye kaydının döküman tipi 'other' (diğer) seçilmiş ise , döküman tipi tanımlayıcısı da bulunmalıdır.</assert>
			<assert test="not(normalize-space(gl-cor:documentType) = 'invoice') or (string-length(normalize-space(gl-cor:documentNumber)) > 0 and gl-cor:documentDate)">Yevmiye kayıtlarında döküman tipi 'invoice' (fatura) seçili ise, döküman no'su ve döküman tarihi de bulunmalıdır.</assert>
			<assert test="not(normalize-space(gl-cor:documentType) = 'check') or (string-length(normalize-space(gl-cor:documentNumber)) > 0 and gl-cor:documentDate)">Yevmiye kayıtlarında döküman tipi 'check' (çek) seçili ise, döküman no'su ve döküman tarihi de bulunmalıdır.</assert>
			<assert test="not(normalize-space(gl-cor:documentType) = 'other') or (string-length(normalize-space(gl-cor:documentNumber)) > 0 and gl-cor:documentDate)">Yevmiye kayıtlarında döküman tipi 'other' (diğer) seçili ise, döküman no'su ve döküman tarihi de bulunmalıdır. </assert>
			
			<assert test="not(gl-cor:documentNumber) or gl-cor:documentType">Yevmiye kayıtlarında döküman no'su bulunuyor ise, döküman tipi bulunmalıdır.</assert>
			<assert test="not(gl-cor:documentDate) or gl-cor:documentType">Yevmiye kayıtlarında döküman tarihi bulunuyor ise, döküman tipi bulunmalıdır.</assert>
			
			<assert test="not(gl-cor:documentReference) or not(parent::node()/gl-cor:entryNumber) or gl-cor:documentReference = parent::node()/gl-cor:entryNumber">gl-cor:documentReference elemanının değeri bir üst düzeydeki gl-cor:entryNumber elemanının değerine eşit olmalıdır.</assert>

			<assert test="not(gl-bus:paymentMethod) or string-length(normalize-space(gl-bus:paymentMethod)) > 0 ">gl-bus:paymentMethod elemanı var ise değeri boşluk olmamalıdır.</assert>
		</rule>
	</pattern>

</schema>
