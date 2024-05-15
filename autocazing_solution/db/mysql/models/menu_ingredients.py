from sqlalchemy import *
from sqlalchemy.sql import func
from db.mysql.base import Base

class Menus(Base):
    __tablename__ = 'menu_ingredients'
    menu_id = Column(Integer, primary_key=True, autoincrement=True)
    ingredient_id = Column(Integer, primary_key=True, autoincrement=True)
    login_id = Column(Integer, nullable=False)

    created_at = Column(DateTime, server_default=func.now(), nullable=False)
    updated_at = Column(DateTime, server_default=func.now(), nullable=False, onupdate=func.now())