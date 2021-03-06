import json
from base64 import b64encode

import lxml as lxml
import requests
import time

from requests import cookies

from bb import BestBuy

try:
    from Crypto.PublicKey import RSA
    from Crypto.Cipher import PKCS1_OAEP
except:
    from Cryptodome.PublicKey import RSA
    from Cryptodome.Cipher import PKCS1_OAEP


class BestBuyBot:
    session = requests.Session()

    userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:95.0) Gecko/20100101 Firefox/95.0"

    def check_url(self, skuid):
        zipcode = ""
        storeId = ""
        return f"https://www.bestbuy.com/api/tcfb/model.json?paths=[[\"shop\",\"buttonstate\",\"v5\",\"item\",\"skus\",{skuid},\"conditions\",\"NONE\",\"destinationZipCode\",{zipcode},\"storeId\",{storeId},\"context\",\"cyp\",\"addAll\",\"false\"]]&method=get"

    def __init__(self, skuid, buyerInfo):
        self.bin_number = ""
        self.skuid = skuid
        self.order_id = ""
        self.item_id = ""
        self.buyerInfo = buyerInfo
        if self.is_available():
            self.on_available()
        else:
            print(skuid + " is not available")

    def on_available(self):
        print(self.skuid + " IS AVAILABLE!")
        tas_data = self.get_tas_data()
        print("got tas data")
        self.add_to_cart()
        self.fulfillment()
        token = self.checkout()
        self.checkout_order_2()
        self.validate()
        self.checkout_order_3()
        self.submit_card_auth()
        while True:
            success, jwt = self.submit_order()
            if not success and jwt is not None:
                transaction_id = self.handle_3dsecure(jwt)
                self.submit_card(transaction_id)
            else:
                if success:
                    print("SUCCESS")
                else:
                    print("FAILURE")
                break
        time.sleep(60 * 5)

    def is_available(self):
        headers = {
            'User-Agent': self.userAgent
        }

        try:
            text = requests.get(self.check_url(self.skuid), headers=headers).text
            print(text)
            return "ADD_TO_CART" in text or \
                   "CHECK_STORES" in text
        except Exception as e:
            print(e)
            print("ERROR SCRAPING PAGE!!!")
            return False

    def get_tas_data(self):
        headers = {
            "accept": "*/*",
            "accept-encoding": "gzip, deflate, br",
            "accept-language": "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7",
            "content-type": "application/json",
            "referer": "https://www.bestbuy.com/checkout/r/payment",
            "user-agent": self.userAgent
        }
        while True:
            try:
                r = requests.get("https://www.bestbuy.com/api/csiservice/v2/key/tas", headers=headers)
                # print(r.text)
                return json.loads(r.text)
            except Exception as e:
                print("Error Getting TAS Data(line {} {} {})")

    def add_to_cart(self):
        headers = {
            "accept": "application/json",
            "accept-encoding": "gzip, deflate, br",
            "accept-language": "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7",
            "content-length": "31",
            "content-type": "application/json; charset=UTF-8",
            "origin": "https://www.bestbuy.com",
            # "referer": self.product,
            "user-agent": self.userAgent
        }
        body = {"items": [{"skuId": self.skuid}]}
        while True:
            try:
                response = self.session.post("https://www.bestbuy.com/cart/api/v1/addToCart", json=body,
                                             headers=headers)
                if response.status_code == 200:
                    # print(response.text)
                    # self.line_id = json.loads(response.text)["summaryItems"][0]["lineId"]
                    # print("line_id: " + self.line_id)
                    print("added to cart")
                else:
                    print("error adding to cart")
                    # print(response.text)
                return
            except Exception as e:
                print(e)
                print("couldn't add to cart!!!")
                return

    # def get_cart_id(self):
    #     headers = {
    #         "accept": "application/json, text/javascript, */*; q=0.01",
    #         "accept-encoding": "gzip, deflate, br",
    #         "accept-language": "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7",
    #         "referer": "https://www.bestbuy.com/cart",
    #         "user-agent": self.userAgent,
    #     }
    #     try:
    #         response = self.session.get("https://www.bestbuy.com/cart/json", headers=headers)
    #         response = json.loads(response.text)
    #         cart_id = response["cart"]["lineItems"][0]["id"]
    #         print(cart_id)
    #     except Exception as e:
    #         print(e)

    # def cart_checkout(self):
    #     headers = {
    #         "host": "www.bestbuy.com",
    #         "accept": "application/json, text/javascript, */*; q=0.01",
    #         "accept-encoding": "gzip, deflate, br",
    #         "accept-language": "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7",
    #         "user-agent": self.userAgent,
    #         "Referer": "https://www.bestbuy.com/cart",
    #         "Content-Type": "application/json",
    #         "X-ORDER-ID": self.order_id,  # 422f9ea0-6ad8-11ec-8cc4-11e18009094f
    #         "Origin": "https://www.bestbuy.com",
    #     }
    #
    #     try:
    #         response = self.session.post("https://www.bestbuy.com/cart/checkout", headers=headers)
    #         if response.status_code == 200:
    #             print(response.text)
    #             # response = json.loads(response.text.split("var orderData = ")[1].split(";")[0])
    #             response = json.loads(response.text)
    #             # self.order_id = response["id"]
    #             self.line_id = response["lineItems"][0]["id"]
    #             # TODO get selected fulfillmentType
    #             print("successfully started checkout")
    #             return
    #     except Exception as e:
    #         print(e)
    #         print("error starting checkout!!!")

    def fulfillment(self):
        headers = {
            "accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
            "accept-encoding": "gzip, deflate, br",
            "accept-language": "en-US,en;q=0.9,zh-CN;q=0.8,zh;q=0.7",
            "upgrade-insecure-requests": "1",
            "user-agent": self.userAgent,
        }

        try:
            response = self.session.get("https://www.bestbuy.com/checkout/r/fufillment", headers=headers)
            if response.status_code == 200:
                # print(response.text)
                response = json.loads(response.text.split("var orderData = ")[1].split(";")[0])
                self.order_id = response["id"]
                self.item_id = response["items"][0]["id"]
                print(self.order_id)
                print(self.item_id)
                # TODO get selected fulfillmentType
                print("successfully started checkout")
                return
        except Exception as e:
            print(e)
            print("error starting checkout!!!")

    def checkout(self):
        headers = {
            "Host": "www.bestbuy.com",
            "User-Agent": self.userAgent,
            "Accept": "application/json, text/javascript, */*; q=0.01",
            "Accept-Language": "en-US,en;q=0.5",
            "Accept-Encoding": "gzip, deflate, br",
            "Referer": "https://www.bestbuy.com/cart",
            "Content-Type": "application/json",
            "X-ORDER-ID": self.order_id,  # 422f9ea0-6ad8-11ec-8cc4-11e18009094f
            "Origin": "https://www.bestbuy.com",
        }
        body = None
        try:
            print(headers)
            response = self.session.post("https://www.bestbuy.com/cart/checkout", json=body, headers=headers, allow_redirects=True)
            response = json.loads(response.text)
            token = response["updateData"]["redirectUrl"].split("?")[1]
            # response = response["updateData"]["order"]
            # order_id = response["id"]
            # line_id = response["lineItems"][0]["id"]
            print("token: "+token)
            # print("order_id: "+order_id)
            # print("line_id: "+line_id)
            print("successfully checkout2")
            # self.guest(token)
            return token
        except Exception as e:
            print(e)

    def signin(self, token):
        headers = {
            "Host": "www.bestbuy.com",
            "User-Agent": self.userAgent,
            "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
            "Accept-Language": "en-US,en;q=0.5",
            "Accept-Encoding": "gzip, deflate, br",
            "Referer": "https://www.bestbuy.com/cart",
            "Content-Type": "application/json",
            "Connection": "keep-alive",
            "Upgrade-Insecure-Requests": "1",
            "Sec-Fetch-Dest": "document",
            "Sec-Fetch-Mode": "navigate",
            "Sec-Fetch-Site": "same-origin",
            "Sec-Fetch-User": "?1",
            "TE": "trailers",
        }
        try:
            print("https://www.bestbuy.com/identity/signin?{}".format(token))
            response = self.session.post("https://www.bestbuy.com/identity/signin?{}".format(token), headers=headers, allow_redirects=True)
            # print(response.text)
            if response.status_code == 200:
                print("successfully signin")
            else:
                print(response.text)
        except Exception as e:
            print(e)

    def guest(self, token):
        headers = {
            "Host": "www.bestbuy.com",
            "User-Agent": self.userAgent,
            "Accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
            "Accept-Language": "en-US,en;q=0.5",
            "Accept-Encoding": "gzip, deflate, br",
            "Referer": "https://www.bestbuy.com/identity/signin?{}".format(token),
            "Content-Type": "application/json",
            "Connection": "keep-alive",
            "Upgrade-Insecure-Requests": "1",
        }
        try:
            response = self.session.post("https://www.bestbuy.com/identity/guest?{}".format(token), headers=headers, allow_redirects=True)
            # print(response.text)
            if response.status_code == 200:
                print("successfully guest")
            else:
                print(response.text)
        except Exception as e:
            print(e)

    def fast_track(self, token):
        headers = {
            "Host": "www.bestbuy.com",
            "accept": "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8",
            "accept-encoding": "gzip, deflate, br",
            "accept-language": "en-US,en;q=0.5",
            "upgrade-insecure-requests": "1",
            "Referer": "https://www.bestbuy.com/identity/signin?{}".format(token),
        }
        try:
            response = self.session.get("https://www.bestbuy.com/checkout/r/fast-track", headers=headers, allow_redirects=True)
            if response.status_code == 200:
                print(response.text)
                # TODO get selected fulfillmentType
                print("successfully got fast track")
                return
            else:
                print("error getting fast track")
                print(response.text)
        except Exception as e:
            print(e)
            print("error starting checkout!!!")

    def checkout_order(self):
        headers = {
            "Host": "www.bestbuy.com",
            "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:95.0) Gecko/20100101 Firefox/95.0",
            "Accept": "application/com.bestbuy.order+json",
            "Accept-Language": "en-US,en;q=0.5",
            "Accept-Encoding": "gzip, deflate, br",
            "Referer": "https://www.bestbuy.com/checkout/r/fulfillment",
            "Content-Type": "application/json",
            "X-User-Interface": "DotCom-Optimized",
            "Origin": "https://www.bestbuy.com",
            "Content-Length": "447",
            "DNT": "1",
            "Connection": "keep-alive",
            "Sec-Fetch-Dest": "empty",
            "Sec-Fetch-Mode": "cors",
            "Sec-Fetch-Site": "same-origin",
        }
        body = {
            "items":
                [
                    {
                        "id": self.item_id,  # TODO
                        "type": "DEFAULT",
                        "selectedFulfillment":
                            {
                                "shipping":
                                    {
                                        "address":
                                            {
                                                "country": "US",
                                                "saveToProfile": False,
                                                # "saveToProfile": "false",
                                                "street2": self.buyerInfo["shipping_a2"],
                                                "useAddressAsBilling": True,
                                                # "useAddressAsBilling": "true",
                                                "middleInitial": "",
                                                "lastName": self.buyerInfo["shipping_lname"],
                                                "street": self.buyerInfo["shipping_a1"],
                                                "city": self.buyerInfo["shipping_city"],
                                                "override": False,
                                                # "override": "false",
                                                "zipcode": self.buyerInfo["shipping_zipcode"],
                                                "state": self.buyerInfo["shipping_state"],
                                                "firstName": self.buyerInfo["shipping_fname"],
                                                "isWishListAddress": False,
                                                # "isWishListAddress": "false",
                                                "dayPhoneNumber": self.buyerInfo["shipping_phone"],
                                                "type": "RESIDENTIAL"
                                            }
                                    }
                            },
                        "giftMessageSelected": False
                        # "giftMessageSelected": "false"
                    }
                ]
        }
        try:
            print(body)
            print("https://www.bestbuy.com/checkout/orders/{}/".format(self.order_id))
            self.session.cookies.set_cookie(cookies.create_cookie(name="c2", value="Checkout: Checkout - Shipping and Pickup Options"))
            # for cookie in self.session.cookies:
            #     print(cookie)
            response = self.session.patch(f"https://www.bestbuy.com/checkout/orders/{self.order_id}/", json=body, headers=headers)
            # TODO get customer order id
            print(response.text)
            print("checkout order succeeded")
        except Exception as e:
            print(e)

    def checkout_order_2(self):
        headers = {
            "Host": "www.bestbuy.com",
            "User-Agent": self.userAgent,
            "Accept": "application/com.bestbuy.order+json",
            "Accept-Language": "en-US,en;q=0.5",
            "Accept-Encoding": "gzip,deflate,br",
            "Referer": "https://www.bestbuy.com/checkout/r/fulfillment",
            "Content-Type": "application/json",
            "X-User-Interface": "DotCom-Optimized",
            "Origin": "https://www.bestbuy.com",
        }
        body = {
            "phoneNumber": self.buyerInfo["shipping_phone"],
            "smsNotifyNumber": "",
            "smsOptIn": False,
            "emailAddress": self.buyerInfo["shipping_email"]
        }
        try:
            print(body)
            print(f"https://www.bestbuy.com/checkout/orders/{self.order_id}/")
            response = self.session.patch(f"https://www.bestbuy.com/checkout/orders/{self.order_id}/",
                                          json=body, headers=headers)
            print(response.text)
            print("checkout order 2 succeeded")
        except Exception as e:
            print(e)

    def validate(self):
        print("not implemented")

    # def tas(self):
    #     response = self.session.get("https://www.bestbuy.com/api/csiservice/v2/key/tas")
    #     response = json.loads(response.text)
    #     # return response["publicKey"], response["keyId"]
    #     return response

    def payment(self, encrypted_card, bin_number):
        headers = {
            "Host": "www.bestbuy.com",
            "User-Agent": self.userAgent,
            "Accept": "application/json, text/javascript, */*; q=0.01",
            "Accept-Language": "en-US,en;q=0.5",
            "Accept-Encoding": "gzip, deflate, br",
            "Referer": "https://www.bestbuy.com/checkout/r/payment",
            "Content-Type": "application/json",
            "X-CLIENT": "CHECKOUT",
            "X-CONTEXT-ID": self.order_id,
            "Origin": "https://www.bestbuy.com",
        }
        body = {
            {"billingAddress": {"country": "US",
                                "useAddressAsBilling": True,
                                "middleInitial": "",
                                "lastName": self.buyerInfo["shipping_lname"],
                                "isWishListAddress": False,
                                "city": self.buyerInfo["shipping_city"],
                                "state": self.buyerInfo["shipping_state"],
                                "firstName": self.buyerInfo["shipping_fname"],
                                "addressLine1": self.buyerInfo["shipping_a1"],
                                "addressLine2": self.buyerInfo["shipping_a2"],
                                "dayPhone": self.buyerInfo["shipping_phone"],
                                "postalCode": self.buyerInfo["shipping_zipcode"],
                                "standardized": True,
                                "userOverridden": False},
             "creditCard": {"hasCID": False, "invalidCard": False, "isCustomerCard": False, "isNewCard": True,
                            "isVisaCheckout": False, "govPurchaseCard": False,
                            "number": encrypted_card,
                            "binNumber": bin_number,
                            "isPWPRegistered": False,
                            "expMonth": self.buyerInfo["card_month"],
                            "expYear": self.buyerInfo["card_year"],
                            "cvv": self.buyerInfo["card_month"],
                            "orderId": "BBY01-806560762417",  # TODO
                            "saveToProfile": False,
                            "type": self.buyerInfo["card_type"],
                            "international": False, "virtualCard": False}}
        }
        try:
            self.session.put(f"https://www.bestbuy.com/payment/api/v1/payment/{self.order_id}/creditCard", json=body, headers=headers)
        except Exception as e:
            print(e)

    def checkout_order_3(self):
        headers = {
            "Host": "www.bestbuy.com",
            "User-Agent": self.userAgent,
            "Accept": "application/com.bestbuy.order+json",
            "Accept-Language": "en-US,en;q=0.5",
            "Accept-Encoding": "gzip, deflate, br",
            "Referer": "https://www.bestbuy.com/checkout/r/payment",
            "Content-Type": "application/json",
            "X-User-Interface": "DotCom-Optimized",
            "Origin": "https://www.bestbuy.com",
        }
        body = {
            {
                "browserInfo":
                    {
                        "javaEnabled": False,
                        "language": "en-US",
                        "userAgent": self.userAgent,
                        "height": "864",
                        "width": "864",
                        "timeZone": "420",
                        "colorDepth": "24"
                    }
            }
        }
        try:
            response = self.session.patch(f"https://www.bestbuy.com/checkout/orders/{self.order_id}/",
                                          json=body, headers=headers)
            print(response.text)
            response = json.loads(response.text)
            try:
                response = response["errors"][0]
                if response["errorCode"] == "PAY_SECURE_REDIRECT":
                    print("3DSecure Found, Starting Auth Process")
                    return False, response["paySecureResponse"]["stepUpJwt"]
            except Exception as e:
                print(e)
                if response["state"] == "SUBMITTED":
                    return True, None
            print("Payment Failed")
            return False, None
        except Exception as e:
            print(e)

    def submit_card_auth(self):
        print("not implemented")

if __name__ == '__main__':
    checked_sku_ids = [
        ""
    ]
    for sku_id in checked_sku_ids:
        BestBuyBot(sku_id, {
            "shipping_a1": "",
            "shipping_a2": "",
            "shipping_fname": "",
            "shipping_lname": "",
            "shipping_city": "",
            "shipping_zipcode": "",
            "shipping_state": "",
            "shipping_phone": "",
            "shipping_email": "",

            "card_number": "",
            "card_type": "",
            "card_cvv": "",
            "card_month": "",
            "card_year": "",
        })
