import requests
import re
import pymysql
from bs4 import BeautifulSoup
import json
import time

#创建连接
conn = pymysql.connect(
    host='111.230.15.168',   #主机IP
    port=3306,              #端口
    user='hzzone',             #连接数据库用户
    password='123456',      #连接密码
    db='test',                #连接的数据库名称
    charset='utf8'
)


product_dict = {
    "手机数码": ["电脑", "平板", "手机", "相机"],
    "家用电器": ["电视", "空调", "洗衣机", "冰箱"],
    "母婴用品": ["奶粉", "辅食", "玩具", "喂养用品"],
    "图书音像": ["教育", "文艺", "杂志", "音像"],
    "女装男装": ["女装", "男装", "内衣", "配饰"],
    "食品酒类": ["水果", "肉类", "饮料", "名酒"],
}

class Product:
    def __init__(self, product_id):
        self.product_id = product_id
        self.image_list = []
        self.left_totals = 100
        self.product_description = ''
        self.product_name = ''
        self.second_category_name = ''
        self.product_price = 100.0

    def get(self):
        return (self.product_id, self.product_name, self.second_category_name, self.product_price, self.product_description, self.left_totals, )


def genRandomString(length=50):
    import random
    import string
    return ''.join(random.choice(string.ascii_letters + string.digits) for _ in range(length))

def searchByKeyword(keyword, total_page=2):
    url_list = []
    for page_number in range(1, total_page+1):
        url = "https://www.amazon.cn/s/ref=sr_pg_2?rh=i:aps,k:%s&page=%s&keywords=%s&ie=UTF8" % (keyword, page_number, keyword)
        content = requests.get(url).content.decode()
        soup = BeautifulSoup(content)
        result = soup.find_all('a', class_=['a-link-normal', 'a-text-normal'])
        # pattern = re.compile('<a class="a-link-normal a-text-normal" target="_blank" href="(.*)"><img alt=')
        # result = re.findall(pattern, content)
        [url_list.append(x['href']) for x in result]
        # [url_list.append(x['href']) for x in result]
        time.sleep(1)

    print(keyword)

    return set(url_list)


def getProductInfo(product_url, second_category_name):
    try:
        content = requests.get(product_url).content.decode()
        product = Product(genRandomString())
        soup = BeautifulSoup(content)
        product.product_name = soup.find_all(id="productTitle")[0].text.strip()
        # print(product.product_name)
        product.second_category_name = second_category_name
        price_str = re.sub(",", "", soup.find_all("span", class_=['a-size-medium', 'a-color-price'])[0].text.strip()[1:])
        product.product_price = float(price_str)
        # img_url = re.findall('<img alt="" src="(.*)._SX38_SY50_CR,0,0,38,50_.jpg"/>]', content)
        # rrr= soup.find_all(id='a-autoid-8-announce')
        for span in soup.find_all('span', class_=['a-button-text']):
            img = span.find_all('img')
            if len(img):
                product.image_list.append(img[0]['src'].strip('._SX38_SY50_CR,0,0,38,50_.jpg')+'._SL800_.jpg')

        des = {}
        table = soup.find_all('div', class_=['pdTab', ])[0]
        labels = table.find_all('td', class_=['label', ])
        values = table.find_all('td', class_=['value', ])
        for x, y in zip(labels, values):
            des[x.text] = y.text
        product.product_description = json.dumps(des, ensure_ascii=False)
        time.sleep(1)
        print(product.product_name)
        return product
    except:
        return None





def insertIntoDatabase():
    # 创建游标
    cursor = conn.cursor()
    i = 1
    for key in product_dict:
        # first_categort_id = genRandomString()
        # cursor.execute("INSERT INTO first_category VALUE (%s, %s);", (key, first_categort_id, ))
        for second_category_name in product_dict[key]:
            # second_category_id = genRandomString()
            rows = cursor.execute("SELECT second_category_id FROM second_category WHERE second_category.second_category_name=%s;", (second_category_name))
            second_category_id = cursor.fetchone()[0]
            # cursor.execute("INSERT INTO second_category VALUE (%s, %s, %s);", (second_category_id, first_categort_id, second_category_name, ))
            for url in searchByKeyword(second_category_name, total_page=3):
                p = getProductInfo(url, second_category_name)
                if p:
                    cursor.execute("INSERT INTO product VALUE (%s, %s, %s, %s, %s, %s);", (p.product_id,
                                   p.product_name, second_category_id, p.product_price, p.product_description, p.left_totals))
                    for img in p.image_list:
                        try:
                            cursor.execute("INSERT INTO image VALUE (%s, %s, %s);", (img, p.product_id, img))
                        except:
                            pass

                    # print(i, p.get())
                    print(i)

                    conn.commit()
                    i = i+1
                else:
                    continue
    time.sleep(10)

    cursor.close()


if __name__ == "__main__":
    # result = searchByKeyword("手机")
    # print(result)
    # print(len(result))

    # product = getProductInfo("https://www.amazon.cn/dp/B07989DSVQ", "手机")
    #
    # print(product.product_description)

    insertIntoDatabase()




    conn.close()

