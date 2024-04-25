import pandas as pd
from statsmodels.tsa.seasonal import seasonal_decompose
from neuralprophet import NeuralProphet, set_log_level
import matplotlib.pyplot as plt

# 시계열 데이터 읽기
data = pd.read_csv('seasonal_decompose.csv')
# 'date' 열을 날짜 형식으로 변환
data['date'] = pd.to_datetime(data['date'], format='%Y%m')

# 분류방법 : addictive 방식, period = 12 (월간 데이터), 주간 별로 계산할려면 period = 7
result = seasonal_decompose(data['price'], model='addictive', period=12) 

# 비교적 가볍고 정확한 신경망 시계열 분석 모델인 Neural Prophet 사용
# 데이터를 학습용, 검증용으로 쪼개야 함
# Arima 모델은 과거의 데이터의 의존하여 에측하는 경향이 있다
# NeuralProphet 모델은 딥러닝 기반의 모델
# 단순하고 간단한 시계열 데이터라면 Arima가 처리속도가 빠르고 정확도도 괜찮다
# 다소 복잡하거나 장기적인 에측이 필요할때는 NeuralProphet 이 더 유연하게 대응 가능하다
# 시간 좀 걸리는거 감안해도 NeuralProphet 쓰는게 정확도 더 높을 듯?
# =============================================================

train_df =  data[data['date'] < '2023-01-01']
test_df = data[data['date'] >= '2023-01-01']

# train용 데이터 생성
train = pd.DataFrame({"ds": pd.to_datetime(train_df["date"], format='%Y%m'), "y": train_df["price"]})
test = pd.DataFrame({"ds": pd.to_datetime(test_df["date"], format='%Y%m'), "y": test_df["price"]})

#모델 설정 및 learning rate 설정, 학습
model = NeuralProphet(
    learning_rate=0.1, 
)

metrics = model.fit(train, freq="MS")

# 다음 1년치를 예측합니다.
future = model.make_future_dataframe(train, periods=12)
forecast = model.predict(future)

# 예측값 배열을 출력합니다.
resultDate = forecast['ds'].values
resultValue = forecast['yhat1'].values
print(resultDate) # 에측한 값의 시기
print(resultValue) # 예측한 값