import requests
import re
import pymysql
from bs4 import BeautifulSoup

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

def insertIntoDatabase():
    # 创建游标
    cursor = conn.cursor()
    for key in product_dict:
        first_categort_id = genRandomString()
        cursor.execute("INSERT INTO first_category VALUE (%s, %s);", (key, first_categort_id, ))
        for second_category_name in product_dict[key]:
            second_category_id = genRandomString()
            cursor.execute("INSERT INTO second_category VALUE (%s, %s, %s);", (second_category_id, first_categort_id, second_category_name, ))

    conn.commit()
    cursor.close()


class Product:
    def __init__(self, product_id):
        self.product_id = product_id
        self.image_list = []
        self.left_totals = 100
        self.product_description = ''
        self.product_name = ''
        self.second_category_name = ''
        self.product_price = 100.0


def genRandomString(length=50):
    import random
    import string
    return ''.join(random.choice(string.ascii_letters + string.digits) for _ in range(length))

def searchByKeyword(keyword, total_page=2):
    url_list = []
    for page_number in range(1, total_page+1):
        url = "https://www.amazon.cn/s/ref=sr_pg_2?rh=i:aps,k:手机&page=%s&keywords=%s&ie=UTF8" % (page_number, keyword)
        response = requests.get(url)
        pattern = re.compile('<a class="a-link-normal a-text-normal" target="_blank" href="(.*)"><img alt=')
        result = re.findall(pattern, response.content.decode())
        [url_list.append(x) for x in result]

    return set(url_list)


def getProductInfo(product_url, second_category_name):
    content = requests.get(product_url).content.decode()
    product = Product(genRandomString())
    soup = BeautifulSoup(content)
    product.product_name = soup.find_all(id="productTitle")[0].text.strip()
    product.second_category_name = second_category_name
    print(soup.find_all("spam", id="priceblock_ourprice"))
    product.product_price = float(re.sub("\D", "", soup.find(id="priceblock_ourprice")[0].text.strip()))

    return product
    # 522_image = "https://images-cn.ssl-images-amazon.com/images/I/51TEg+W1cDL._SX522_.jpg"

if __name__ == "__main__":
    # result = searchByKeyword("手机")
    # print(result)
    # print(len(result))

    product = getProductInfo("https://www.amazon.cn/dp/B07989DSVQ", "手机")

    print(product)

    # insertIntoDatabase()




    conn.close()

