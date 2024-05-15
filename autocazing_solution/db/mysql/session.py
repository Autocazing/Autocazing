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

class MySQLSession:
    def __init__(self):
        self.settings = get_setting()
        self.SQLALCHEMY_DATABASE_URL = 'mysql+pymysql://{}:{}@{}:{}/{}'.format(
            self.settings.MYSQL_USER,
            self.settings.MYSQL_PASSWORD,
            self.settings.MYSQL_HOST,
            self.settings.MYSQL_PORT,
            self.settings.MYSQL_DATABASE
        )
        self.engine = create_engine(self.SQLALCHEMY_DATABASE_URL, echo=True)
        self.SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=self.engine)
        
        # 개발 환경에서만 테이블 자동 생성
        if self.settings.ENVIRONMENT == "local":
            Base.metadata.create_all(self.engine)
    
    def get_db(self):
        db = self.SessionLocal()
        try:
            yield db
        finally:
            db.close()

mysqlSession = MySQLSession()