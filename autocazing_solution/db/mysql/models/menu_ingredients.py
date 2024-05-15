from sqlalchemy import *
from sqlalchemy.sql import func
from db.mysql.base import Base

class MenuIngredients(Base):
    __tablename__ = 'menu_ingredients'
    menu_id = Column(Integer, primary_key=True)
    ingredient_id = Column(Integer, primary_key=True)
    login_id = Column(String(length=20), nullable=False)
    capacity = Column(Integer, nullable=False)

    created_at = Column(DateTime, server_default=func.now(), nullable=False)
    updated_at = Column(DateTime, server_default=func.now(), nullable=False, onupdate=func.now())