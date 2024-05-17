from sqlalchemy import *
from sqlalchemy.orm import relationship
from sqlalchemy.sql import func
from db.mysql.base import Base

# class MenuSalesSpecific(Base):
#     __tablename__ = 'menu_sales_specific'
#     report_id = Column(Integer, ForeignKey('reports.report_id'), nullable=False, primary_key=True)
class IngredientSolution(Base):
    __tablename__ = 'ingredient_solution'
    report_id = Column(Integer, ForeignKey('reports.report_id'), nullable=False, primary_key=True)
    ingredient_id = Column(Integer, nullable=False, primary_key=True)
    menu_name = Column(String(length=20), nullable=False, primary_key=True) # 판매할 재료
    sale_quantity = Column(Integer, nullable=False) # 판매해야하는 양

class ExpirationSpecifics(Base): # 유통기한 임박 재료
    __tablename__ = 'expiration_specifics'
    report_id = Column(Integer, ForeignKey('reports.report_id'), nullable=False, primary_key=True)
    ingredient_id = Column(Integer, nullable=False)
    ingredient_name = Column(String(length=20), nullable=False, primary_key=True)

class OnDeliveryIngredients(Base):  # 배송 중인 재료
    __tablename__ = 'on_delivery_ingredients'
    report_id = Column(Integer, ForeignKey('reports.report_id'), nullable=False, primary_key=True)
    ingredient_name = Column(String(length=20), nullable=False, primary_key=True)

class Reports(Base):    # 리포트
    __tablename__ = 'reports'
    report_id = Column(Integer, primary_key=True, index=True, autoincrement=True)
    login_id = Column(Integer, nullable=False)
    expected_monthly_sales = Column(Integer, nullable=False)
    current_monthly_sales = Column(Integer, nullable=False) # 일단 애매함. 매번 새로운 값을 반영해야 하는데...
    expiration_specifics = relationship("ExpirationSpecifics", cascade="all, delete-orphan")
    on_delivery_ingredients = relationship("OnDeliveryIngredients", cascade="all, delete-orphan")
    
    created_at = Column(DateTime, server_default=func.now(), nullable=False)
    updated_at = Column(DateTime, server_default=func.now(), nullable=False, onupdate=func.now())
    