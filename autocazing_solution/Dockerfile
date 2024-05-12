FROM python:3.11.1

WORKDIR /autocazing_solution

COPY requirements.txt /autocazing_solution/requirements.txt

RUN pip install --no-cache-dir --upgrade -r /autocazing_solution/requirements.txt

COPY . /autocazing_solution

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8088"]