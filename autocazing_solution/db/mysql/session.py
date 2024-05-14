import logging
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from core.config import get_setting
from db.mysql.models.ingredients import Ingredients
from db.mysql.models.menus import Menus
from db.mysql.models.orders import OrderSpecifics, Orders
from db.mysql.models.restock_orders import RestockOrderSpecifics, RestockOrders
from db.mysql.models.reports import ExpirationSpecifics, OnDeliveryIngredients, Reports

from db.mysql.base import Base

logging.basicConfig()
logging.getLogger('sqlalchemy.engine').setLevel(logging.INFO)

settings = get_setting()    # config.py에 설정해놓은 setting을 가져옴

SQLALCHEMY_DATABASE_URL = 'mysql+pymysql://{}:{}@{}:{}/{}'.format(
    settings.MYSQL_USER,
    settings.MYSQL_PASSWORD,
    settings.MYSQL_HOST,
    settings.MYSQL_PORT,
    settings.MYSQL_DATABASE
)

db_engine = create_engine(SQLALCHEMY_DATABASE_URL, echo=True)  # data base에 연결할 엔진 생성

SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=db_engine)  # 위의 엔진을 이용해 세션 팩토리 생성