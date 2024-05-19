from db.mysql.session import mysqlSession

# Dependency injection for database session
def get_db():
    return next(mysqlSession.get_db())