class BraimSessionsController < ApplicationController
  before_action :set_braim_session, only: [:show, :edit, :update, :destroy , :raw, :affectiv]

  respond_to :html

  def index
    @braim_sessions = BraimSession.all
    respond_with(@braim_sessions)
  end

  def show
    respond_with(@braim_session)
  end

  def raw
    @epoc_entries = @braim_session.epoc_entries
    @player_entries = @braim_session.player_entries 
    @data_node_0 = @epoc_entries.map {|entry| [entry.timestamp.to_i,entry.nodes[0]]}
    @data_node_1 = @epoc_entries.map {|entry| [entry.timestamp.to_f,entry.nodes[1]]}
    @data_node_2 = @epoc_entries.map {|entry| [entry.timestamp.to_f,entry.nodes[2]]}
    @data_node_3 = @epoc_entries.map {|entry| [entry.timestamp.to_f,entry.nodes[3]]}
    @data_node_4 = @epoc_entries.map {|entry| [entry.timestamp.to_f,entry.nodes[4]]}
    @data_node_5 = @epoc_entries.map {|entry| [entry.timestamp.to_f,entry.nodes[5]]}
    @data_node_6 = @epoc_entries.map {|entry| [entry.timestamp.to_f,entry.nodes[6]]}
    @data_node_7 = @epoc_entries.map {|entry| [entry.timestamp.to_f,entry.nodes[7]]}
    @data_node_8 = @epoc_entries.map {|entry| [entry.timestamp.to_f,entry.nodes[8]]}
    @data_node_9 = @epoc_entries.map {|entry| [entry.timestamp.to_f,entry.nodes[9]]}
    @data_node_10 = @epoc_entries.map {|entry| [entry.timestamp.to_f,entry.nodes[10]]}
    @data_node_11 = @epoc_entries.map {|entry| [entry.timestamp.to_f,entry.nodes[11]]}
    @data_node_12 = @epoc_entries.map {|entry| [entry.timestamp.to_f,entry.nodes[12]]}
    @data_node_13 = @epoc_entries.map {|entry| [entry.timestamp.to_f,entry.nodes[13]]}
    @player_timeline = @player_entries.map {|entry| [entry.timestamp.to_f,entry.player_state]}
    render :show
  end

  def affectiv
    @epoc_entries = @braim_session.epoc_entries
    @player_entries = @braim_session.player_entries 
    @data_excitement = @epoc_entries.map {|entry| [entry.timestamp.to_f,entry.excitement.to_f]}
    @data_frustration = @epoc_entries.map {|entry| [entry.timestamp.to_f,entry.frustration.to_f]}
    @data_meditation = @epoc_entries.map {|entry| [entry.timestamp.to_f,entry.meditation.to_f]}
    @data_engagement = @epoc_entries.map {|entry| [entry.timestamp.to_f,entry.engagement.to_f]}
    @player_timeline = @player_entries.map {|entry| [entry.timestamp.to_f,entry.player_state]}
    render :show
  end

  def new
    @braim_session = BraimSession.new
    respond_with(@braim_session)
  end

  def edit
  end

  def create
    @braim_session = BraimSession.new(braim_session_params)
    @braim_session.save
    respond_with(@braim_session)
  end

  def update
    @braim_session.update(braim_session_params)
    respond_with(@braim_session)
  end

  def destroy
    @braim_session.destroy
    respond_with(@braim_session)
  end

  private
    def set_braim_session
      @braim_session = BraimSession.find(params[:id])
    end

    def braim_session_params
      params[:braim_session]
    end
end
