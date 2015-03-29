describe 'Ping', :type => :request do
  it 'ping' do
    get '/api/v1/ping'
    expect(response.body).to eq({ ping: 'pong' }.to_json)
  end

  it 'ping with a parameter' do
    get '/api/v1/ping?pong=test'
    expect(response.body).to eq({ ping: 'test' }.to_json)
  end

  context 'protected', :keep_db do

    before :context do
      FactoryGirl.create :user, email: 'allam.britto@fake.com', password: '12345678', password_confirmation: '12345678'
      get '/api/v1/token', %Q{data=allam.britto@fake.com&password=12345678}
      @credentials = JSON.parse response.body
    end

    context 'ping when credentials are valid' do
      it 'ping' do
        get '/api/v1/protected_ping', "braim_token=#{@credentials['access_token']}"
        expect(response.body).to eq({ ping: 'pong' }.to_json)
      end
    end

    context 'ping when credentials are invalid' do
      it 'returns 401 - unauthorized' do
        expected_response = { error: '401 Unauthorized' }
        get '/api/v1/protected_ping', "braim_token=#{@credentials['access_token'].reverse}"
        expect(response.status).to eq 401
        expect(response.body).to match JSON.dump expected_response
      end
    end

  end

end
