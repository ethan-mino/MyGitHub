#-*- coding: utf-8 -*-
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException
from selenium.common.exceptions import NoSuchElementException
import os
import urllib.request
import time # 실행시간 측정을 위해 import
import datetime #실행시간을 시/분/초로 변환하기 위해 import

# C:/Users/Administrator/google_image_crawling/chromedriver.exe (내 컴퓨터 chrome driver 위치)
#******************************************************************
# 코드 실행 전 chrome버전에 맞는 chrome driver 다운로드 (다운로드 url https://sites.google.com/a/chromium.org/chromedriver/downloads)
# (pip install 안될 때는 openssl 다운로드 하거나, path 설정)
# https://bassyun.tistory.com/26, https://kauboy.tistory.com/5 (icrawler 사용)
# https://j-remind.tistory.com/61 (selenium 사용)
# https://github.com/Zikx/googleImageCrawler/blob/master/image_crawler.py. https://eneun.tistory.com/10, https://data-make.tistory.com/172, https://icrawler.readthedocs.io/en/latest/builtin.html#search-engine-crawlers (google_images_download 사용- 100개 이상 다운로드 안됨 - https://pypi.org/project/google_images_download/)
# https://coldsewoo.com/blog/category/web/backend/node.js/20190811001 (puppeteer 사용)
# http://guswnsxodlf.github.io/check-pep8-on-pycharm (파이참(pycharm)에서 pep8 가이드 검사하기)
# WebDriver API (https://selenium-python.readthedocs.io/api.html)
#******************************************************************

delay = 10 # wait 최대 second
def wait_by_selector(selector) : # Selenium을 사용해 테스트를 할때 element를 찾을 수 있도록 Web Page가 로딩이 끝날때 까지 기다려야 함. AJAX를 이용해 만든 Web의 경우 리소스가 로드하는데 부문별로 다를 수 있음.
    try:    # https://stackoverflow.com/questions/26566799/wait-until-page-is-loaded-with-selenium-webdriver-for-python, https://www.fun-coding.org/crawl_advance6.html 참고
        myElem = WebDriverWait(browser, delay).until(EC.presence_of_element_located((By.CSS_SELECTOR , selector))) # 검색 페이지로 이동한 후 footer가 존재할 때까지 wait
        print("Page is ready!")
    except TimeoutException:
        print("Loading took too much time!")
        total_execution_time = time.time() - total_start  # 프로그램 전체 실행시간
        print("total execution time : ", str(datetime.timedelta(seconds=total_execution_time)))  # 프로그램 전체 실행시간을 시/분/초로 출력.

def page_scroll():
    more_button = None
    footer = browser.find_element_by_css_selector('footer[class = "Pvpvv DM4yme"]')  # footer가 안보이면 끝에 다다르지 않은 것, 끝에 다다르면 결과 더보기를 클릭.

    for input_element in browser.find_elements_by_css_selector('input'):  # "결과 더보기" 버튼 찾기 (value가 "결과 더보기인" input 태그 찾음)
        value = input_element.get_attribute("value")
        if value == "결과 더보기" :
            more_button = input_element  # more_button은 "결과 더보기" 버튼
            break

    # print(footer)  # 테스트용

    for _ in range(300):  # 스크롤 내림.
        browser.execute_script("window.scrollBy(0,10000)")  # 스크롤 10000픽셀 내림.
        # print(footer.is_displayed())   # 테스트용

        if footer.is_displayed() == True and more_button.is_displayed() == True :   # https://stackoverflow.com/questions/15937966/in-python-selenium-how-does-one-find-the-visibility-of-an-element 참고
            more_button.click()                                                     # https://stackoverflow.com/questions/34380119/isdisplayed-vs-isvisible-in-selenium 참고
                                                                                    # 스크롤을 내리다가 끝에 다다라서 footer 요소의 visibility: hidden -> visivle로 변하고, 결과 더보기 버튼의 display 속성을 확인하고 None이 아니면 클릭한다.

def push_url_to_list() :
    url_list = []
    elements = browser.find_elements_by_css_selector('a[class = "wXeWr islib nfEiy mM5pbd"]')
    for a in elements:
        a.click()
        url_list.append(a.get_attribute("href"))  # 각 이미지의 a 태그의 href 속성을 리스트에 저장.(현재 사이트의 모든 이미지를 다운 받고, 리스트에 있는 url로 이동한 후, 더보기를 누르고 다시 이미지를 크롤링한다. ////)
    print("url_list : " + str(url_list))  # url의 개수 출력
    print("url_list len : " + str(len(url_list)))    # url의 개수 출력
    return url_list

def page_image_download() :
    global counter, succounter

    for x in browser.find_elements_by_css_selector('img.rg_i'):
        counter = counter + 1   # counter는 이미지의 총 개수
        # print("Total Count : " + str(counter))
        # print("Succsessful Count : " + str(succounter))   # succounter는 다운로드에 성공한 이미지의 개수
        # print("URL : " + json.loads(x.get_attribute('innerHTML'))["ou"])

        img_url = x.get_attribute('data-src')
        # img_type = x.get_attribute('src').split("/")[1].split(";")[0]

        if img_url == None :
            img_url = x.get_attribute('src')    # 이미지의 data-src 속성이 None인 경우가 있음, 이 경우에는 이미지의 url을 src 속성값으로 한다.
        #print(img)

        try:
            req = urllib.request.Request(img_url)
            raw_img = urllib.request.urlopen(req).read()
            file = open(os.path.join(searchterm, searchterm + "_" + str(counter) + ".jpg"), "wb")
            file.write(raw_img)
            file.close()
            succounter = succounter + 1
        except Exception as ex:
            print("in page_image_download : " + str(ex))
            total_execution_time = time.time() - total_start  # 프로그램 전체 실행시간
            print("total execution time : ", str(datetime.timedelta(seconds=total_execution_time)))  # 프로그램 전체 실행시간을 시/분/초로 출력.

    print(str(succounter) + " pictures succesfully downloaded") # 다운받은 이미지 개수 출력.

# 크롬 headless 모드 실행 (https://ankiwoong.site/60 참고)
chrome_options = webdriver.ChromeOptions()
chrome_options.add_argument('headless')
chrome_options.add_argument('--disable-gpu')
chrome_options.add_argument('lang=ko_KR')

browser = webdriver.Chrome('./chromedriver.exe', options = chrome_options) # 첫 인자는 chrome driver의 경로, 두번째 인자는 headless를 사용할 경우에만 options = chrome_options)
header = {'User-Agent': "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36"}
options = webdriver.ChromeOptions()
options.add_argument('headless')

counter = 0
succounter = 0
searchterm = '자동차 번호판'  # 폴더 이름 및 검색 내용

if not os.path.exists(searchterm):  # 검색 내용에 대한 폴더가 없으면 생성한다.
    os.mkdir(searchterm)

total_start = time.time()  # 프로그램 시작 시간 저장

url = "https://www.google.co.in/search?q=" + searchterm + "&source=lnms&tbm=isch"
browser.get(url)    #url로 이동

wait_by_selector("#ZCHFDb") # page load 기다림 (ZCHFDb는 footer의 id)
page_scroll()   # 페이지의 끝까지 스크롤한다.

url_list = push_url_to_list()  # 각 이미지와 가장 가까운 a태그의 href 속성값을 list에 저장한 후 리스트를 반환한다.
page_image_download()   # 현재 페이지의 이미지들을 download한다.
print(url_list)

for url in url_list :
    if url != None :
        cur_page_download_start = time.time()  # 현재 페이지를 다운로드하기 시작 전 저장
        print("current page url : " + url)

        try :
            browser.get(url)  # 각 이미지와 가장 가까운 a태그의 href 속성값으로 이동
            wait_by_selector("a[class = 'ZuJDtb']")  # 더보기 a 태그 wait.

            a_more = browser.find_element_by_css_selector("a[class = 'ZuJDtb']")    # 더보기 a 태그 find
            a_more.click()  # 관련 이미지 더보기 클릭.
            wait_by_selector("#ZCHFDb")  # page load 기다림 (ZCHFDb는 footer의 id)

            page_scroll()  # 페이지의 끝까지 스크롤한다.
            page_image_download()  # 현재 페이지의 이미지들을 download한다.

            cur_page_download_time = time.time() - cur_page_download_start  # 실행시간
            print("current page download time : ", str(datetime.timedelta(seconds=cur_page_download_time)))  # 현재 페이지를 다운로드하는데 소요된 시간을 시/분/초로 출력.
        except Exception as ERR :
            print("in for statement : " + str(ERR) + " can not find 'a' tag")
            total_execution_time = time.time() - total_start  # 프로그램 전체 실행시간
            print("total execution time : ", str(datetime.timedelta(seconds=total_execution_time)))  # 프로그램 전체 실행시간을 시/분/초로 출력.

total_execution_time = time.time() - total_start    # 프로그램 전체 실행시간
print("total execution time : ", str(datetime.timedelta(seconds = total_execution_time)))  # 프로그램 전체 실행시간을 시/분/초로 출력.
#browser.close()    # browser를 닫는다.
