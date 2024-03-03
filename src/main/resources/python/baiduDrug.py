import sys
import requests
import json
import re
from urllib.parse import quote
from bs4 import BeautifulSoup



# 百度健康
def get_drug_info(keyword, filename):
    try:
        res_json = {}

        # 测试
        keyword = remove_brackets(keyword)
        print(keyword)
        url = 'https://expert.baidu.com/mall-search/drug/search?page=0&pageSize=1&query={}'
        url2 = 'https://jiankang.baidu.com/tradeflow/b2c/product/detail?skuId={}&storeId={}'

        # 发送请求获取药品信息
        response = requests.get(url.format(keyword))
        json_data = response.json()
        status = json_data['status']
        price = ''
        commonName=''
        indication=''
        headerImages=''
        resultSentence=''
        result_sentence=''
        # 检查请求状态
        if status == 0:
            data = json_data['data']
            if len(data['drugs']) ==0 :
                price = '尚未找到对应的药品'

            else:
                data = json_data['data']
                drug = data['drugs'][0]
                skuId = drug['skuId']
                storeId = drug['pfStoreId']
                # print("skuId->{}, storeId->{} .".format(skuId, storeId))

                # 发送请求获取药品详细信息
                detail_response = requests.get(url2.format(skuId, storeId))
                detail_json = detail_response.json()
                detail_status = detail_json['status']

                # 检查请求状态
                if detail_status == 0:
                    baseInfo = detail_json['data']['baseInfo']
                    commonName = baseInfo['commonName']
                    indication = baseInfo['indication']
                    headerImages = baseInfo['headerImages'][0]
                    drugAbstract = baseInfo['instructionManual']['drugAbstract']

                    price = baseInfo['price']/100
                    # 创建一个空列表用于存储句子
                    sentences = []

                    if drugAbstract is not None:
                        # 遍历 JSON 数组
                        for item in drugAbstract:
                            # 将 "name" 和 "description" 合并为一个句子，并添加到列表中
                            sentence = "{}: {}".format(item['name'], item['description'])
                            sentences.append(sentence)

                    # 使用字符串的 join 方法将句子连接起来
                    result_sentence = "\n".join(sentences)

                    # 打印药品信息
                    # print(commonName)
                    # print(indication)
                    # print(headerImages)
                    # print(result_sentence)

             # 创建一个字典用于存储信息
            res_json = {
                "commonName": commonName,
                "price": price,
                "indication": indication,
                "headerImages": headerImages,
                "resultSentence": result_sentence
            }

            print(res_json)

        keyword = remove_brackets(keyword)
        keyword = quote(keyword, encoding='gbk')  # 使用 gbk 编码
        url = 'https://maiyao.liangxinyao.com/i/asynSearch.htm?_ksTS=1709092049134_116&callback=jsonp117&mid=w-14644508915-0&wid=14644508915&path=/search.htm&q={}&type=p&search=y&newHeader_b=s_from&searcy_type=item&from=liangxinyao.shop.pc_1_placeholder&spm=a1z10.3-b-s.a2227oh.d101'
        headers = {
            'Accept-Language': 'zh-CN,zh;q=0.9',
        }
        response = requests.get(url.format(keyword), headers=headers)
        html_data = response.text


        # 从JSONP响应中提取HTML数据
        match = re.search(r'jsonp117\("(.*)"\)', html_data, re.DOTALL)
        if match:
            html_content = match.group(1)

            # 删除转义字符 \"
            html_content = html_content.replace(r'\"', '"')

            soup = BeautifulSoup(html_content, 'html.parser')

            # 获取所有商品信息
            item = soup.select('.J_TItems .item5line1 .item')[0]
            # 提取商品信息
            product_name = item.select_one('.item-name').text.strip()
            product_price = item.select_one('.c-price').text.strip()
            product_image_url = 'https:'+item.select_one('.photo img')['data-ks-lazyload']


            strong_tag = soup.find('strong')

            if strong_tag and '没找到符合条件的商品,换个条件或关键词试试吧。' in strong_tag.get_text():

                res_json['price2'] = '尚未找到对应的药品'
                res_json['img2'] = ''
            else:
                # 打印商品信息
                print(f"商品名称: {product_name}")
                print(f"商品价格: {product_price}")
                print(f"商品图片URL: {product_image_url}")

                res_json['price2'] = product_price
                res_json['img2'] = product_image_url

        else:
            print("未找到HTML内容。")

        # 将字典转换为 JSON 格式并保存到文件
        print(filename)
        with open(filename, 'w', encoding='utf-8') as json_file:
            json.dump(res_json, json_file, ensure_ascii=False, indent=4)
    except requests.exceptions.RequestException as e:
        print(f"请求失败: {e}")


def remove_brackets(text):
    # 使用正则表达式匹配括号及其内部内容，并替换为空字符串
    cleaned_text = re.sub(r'\([^)]*\)', '', text)
    return cleaned_text.strip()  # 去掉首尾空格

if __name__ == "__main__":
    # 从命令行获取关键字参数
    if len(sys.argv) != 3:
        print("Usage: python script.py <keyword>")
        sys.exit(1)

    keyword = sys.argv[1]
    filename = sys.argv[2]

    get_drug_info(keyword, filename)

