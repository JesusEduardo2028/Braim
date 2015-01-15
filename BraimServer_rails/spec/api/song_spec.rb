describe 'Song' , :type => :request do
	describe :v1 do
		context 'songs', :keep_db do
			before :context do
				@user = FactoryGirl.create :user, email: 'allam.britto@fake.com', password: '12345678', password_confirmation: '12345678'
	      FactoryGirl.create_list(:song, 20)
	      get '/api/v1/token', %Q{email=allam.britto@fake.com&password=12345678}
	      @credentials = JSON.parse response.body
	    end

	    context 'GET' do
	    	it 'verifies that response has the elements number specified in per_page param' do
          token = "songbook_token=#{@credentials['access_token']}"
          per_page = 5
          data = "per_page=#{per_page}"

          get '/api/v1/songs', "#{token}&#{data}"

          expect(response.status).to eq 200
          expect(JSON.parse(response.body).count).to be per_page
        end
	    end
		end
	end
end