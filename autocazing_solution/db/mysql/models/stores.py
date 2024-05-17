from sqlalchemy import *
from db.mysql.base import Base

class Stores(Base):
    __tablename__ = 'stores'
    store_id = Column(Integer, primary_key=true, autoincrement=true)
    login_id = Column(String(length=20), nullable=False)
